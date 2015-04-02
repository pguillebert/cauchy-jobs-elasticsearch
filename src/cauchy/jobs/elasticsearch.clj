(ns cauchy.jobs.elasticsearch
  (:require [clj-http.client :as http]))

(defn fetch-health
  [{:keys [host port] :or {host "localhost" port 9200}}]
  (let [url (str "http://" host ":" port "//_cluster/health")]
    (:body (http/get url {:as :json}))))

(defn fetch-stats
  [{:keys [host port] :or {host "localhost" port 9200}}]
  (let [url (str "http://" host ":" port "/_stats?fielddata=true")]
    (:body (http/get url {:as :json}))))

(defn fetch-local
  [{:keys [host port] :or {host "localhost" port 9200}}]
  (let [url (str "http://" host ":" port "/_nodes/_local/stats")]
    (:body (http/get url {:as :json}))))

(defn fetch-status
  [{:keys [host port] :or {host "localhost" port 9200}}]
  (let [url (str "http://" host ":" port "/_status")]
    (:body (http/get url {:as :json}))))

(defn get-node-id
  [conf]
  (let [nodes-id (->> (fetch-local conf)
                      (:nodes)
                      (keys))]
    (when (= 1 (count nodes-id))
      (name (first nodes-id)))))

(defn count-local-active-shards
  [conf]
  (when-let [node-id (get-node-id conf)]
    (let [status (fetch-status conf)
          shard-infos (for [index (keys (:indices status))
                            shard-num (keys (get-in status [:indices index :shards]))]
                        (get-in status [:indices index :shards shard-num]))]

      (reduce (fn [acc info]
                (if (= node-id (get-in info [:routing :node]))
                  (inc acc)
                  acc))
              0
              (flatten shard-infos)))))

(def color->state
  {"green" "ok"
   "yellow" "warning"
   "red" "critical"})

(defn elasticsearch-health
  ([{:keys [host port] :as conf}]
   (let [{:keys [status active_shards
                 unassigned_shards relocating_shards
                 initializing_shards active_primary_shards]
          :as health} (fetch-health conf)
         stats (fetch-stats conf)]
     [{:service "color" :state (color->state status)}

      {:service "local_active_shards"
       :metric (count-local-active-shards conf)}

      {:service "docs_in_cluster"
       :metric (get-in stats [:_all :primaries :docs :count])}

      {:service "fielddata_memory_size_in_bytes"
       :metric (get-in stats [:_all :total :fielddata :memory_size_in_bytes])}

      {:service "fielddata_evictions"
       :metric (get-in stats [:_all :total :fielddata :evictions])}

      {:service "active_shards" :metric active_shards}
      {:service "unassigned_shards" :metric unassigned_shards}
      {:service "relocating_shards" :metric relocating_shards}
      {:service "initializing_shards" :metric initializing_shards}
      {:service "active_primary_shards" :metric active_primary_shards}]))
  ([] (elasticsearch-health {})))
