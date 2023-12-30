(ns dev
  {:clj-kondo/config {:linters {:unused-namespace {:level :off}}}}
  (:require
   [clojure.tools.namespace.repl :as nsrepl]
   [clojure.walk :as walk]
   [dev.repl :as dev-repl]
   [donut.bakery.backend.system :as sys]
   [donut.endpoint.test.harness :as deth]
   [donut.generate :as dg]
   [donut.generators :as dgs] ;; registor generators
   [donut.routes :as dr]
   [donut.system :as ds]
   [donut.system.repl :as dsr]
   [donut.system.repl.state :as dsrs]
   [malli.core :as m]
   [muuntaja.core :as mu]
   [portal.api :as p]
   [reitit.core :as r]
   [ring.mock.request :as ring-mock])
  (:refer-clojure :exclude [test]))

(nsrepl/set-refresh-dirs "dev/src" "src" "test")

(def instance dsr/instance)

(defn routes
  []
  (r/routes (instance [:routing :router])))

(def start dsr/start)
(def stop dsr/stop)
(def restart dsr/restart)

(defn datasource [] (ds/instance dsrs/system [:db :datasource]))
(defn router [] (get-in dsrs/system [::ds/instances :http :router]))

(defmethod ds/named-system :donut.system/repl
  [_]
  (ds/system :dev))

(when-not dsrs/system
  (dsr/start))
