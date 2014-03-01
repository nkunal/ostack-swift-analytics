(defproject ostack-analytics "1.0.0-SNAPSHOT"
  :description "FIXME: write description"
  :dependencies [[org.clojure/clojure "1.5.1"]
  		 [org.clojure/tools.namespace "0.2.4"]
                 [cascalog "2.0.0"]
		 [org.apache.hadoop/hadoop-core "0.20.2"]
                 ]
  :main logstats.swiftanalytics
  :profiles { :dev {:dependencies [[org.apache.hadoop/hadoop-core "0.20.2"]]}})
