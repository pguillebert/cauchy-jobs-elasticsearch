(defproject cauchy-jobs-elasticsearch "0.1.2"
  :description "Cauchy jobs to monitor Elasticsearch server"
  :url "https://github.com/pguillebert/cauchy-jobs-elasticsearch"
  :scm {:name "git"
        :url "https://github.com/pguillebert/cauchy-jobs-elasticsearch"}
  :pom-addition [:developers
                 [:developer
                  [:name "Philippe Guillebert"]
                  [:url "https://github.com/pguillebert"]
                  [:timezone "+1"]]]
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :signing {:gpg-key "93FEB8D7"}
  :deploy-repositories [["clojars" {:creds :gpg}]]
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/tools.logging "0.3.1"]
                 [org.slf4j/slf4j-api "1.7.12"]
                 [clj-http "1.1.2"]])
