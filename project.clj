(defproject bernhard "0.1.0-SNAPSHOT"
  :description "An interface for Riemann"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :main bernhard.handler
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.9.89"]
                 [org.omcljs/om "1.0.0-alpha38"]
                 [figwheel-sidecar "0.5.4-6" :scope "test"]
                 ;; fuck, dude; riemann pulls in lots that conflicts.
                 ;; [riemann "0.2.11" :exclusions
                 ;;  [
                 ;;   clj-time
                 ;;   com.google.protobuf/protobuf-java
                 ;;   com.fasterxml.jackson.core/jackson-core
                 ;;   org.jsoup/jsoup]]
                 [org.clojure/tools.logging "0.3.1"]
                 [org.immutant/web "2.1.1"]])
