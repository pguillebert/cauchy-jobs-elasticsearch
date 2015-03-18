# cauchy-jobs-elasticsearch

This is an example of a plugin for the [Cauchy](https://github.com/pguillebert/cauchy)
monitoring agent.

It checks the [ElasticSearch](https://www.elastic.co/) index service.

It provides the following metrics :

* color
* active_shards
* unassigned_shards
* relocating_shards
* initializing_shards
* active_primary_shards
* local_active_shards
* docs_in_cluster
* fielddata_memory_size_in_bytes
* fielddata_evictions

## Usage

Just add the JAR to your classpath, add the provided profile configuration
to your cauchy configuration.

## License

Copyright © 2015 Philippe Guillebert

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
