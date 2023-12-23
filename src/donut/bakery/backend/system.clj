(ns donut.bakery.backend.system
  (:require
   [aero.core :as aero]
   [clojure.java.io :as io]
   [donut.bakery.backend.endpoint-routes :as endpoint-routes]
   [donut.endpoint.route-group :as derg]
   [donut.endpoint.route-writer :as derw]
   [donut.endpoint.router :as der]
   [donut.middleware :as dm]
   [donut.system :as ds]
   [next.jdbc :as jdbc]
   [ring.adapter.jetty :as rj]))

(defn env-config [& [profile-name]]
  (-> "donut/bakery/config/env.edn"
      io/resource
      (aero/read-config (when profile-name {:profile profile-name}))
      (assoc :profile-name profile-name)))

(defmethod ds/named-system :base
  [_]
  {::ds/defs
   {:env
    (env-config)

    :middleware
    (assoc dm/MiddlewareComponentGroup :routes endpoint-routes/routes)

    :http
    {:server
     #::ds{:start  (fn [{:keys [::ds/config]}]
                     (rj/run-jetty (:handler config) (:options config)))
           :stop   (fn [{:keys [::ds/instance]}]
                     (.stop instance))
           :config {:handler (ds/local-ref [:handler])
                    :options {:port  (ds/ref [:env :http-port])
                              :join? false}}}

     :handler
     #::ds{:start  (fn [{:keys [::ds/config]}]
                     (let [{:keys [route-ring-handler middleware]} config]
                       (middleware route-ring-handler)))
           :config {:route-ring-handler (ds/ref [:routing :ring-handler])
                    :middleware         (ds/ref [:middleware :donut-middleware])}}}

    :routing
    {:ring-handler der/RingHandlerComponent
     :router       der/RouterComponent
     :router-opts  der/router-opts
     :routes       [(ds/ref [:main-routes :route-group])]}


    :main-routes
    (derg/route-group
     {:path-prefix "/api/v1"
      :shared-opts {:datasource (ds/ref [:db :datasource])}
      :routes      endpoint-routes/routes})

    :db
    {:connection
     #::ds{:start  (fn [{:keys [::ds/config]}]
                     (jdbc/get-datasource (:dbspec config)))
           :config {:dbspec (ds/ref [:env :dbspec])}}}

    :background
    {:route-writer derw/RouteWriterComponent}
    }})

(defmethod ds/named-system :dev
  [_]
  (ds/system :base {[:env] (env-config :dev)}))

(defmethod ds/named-system :test
  [_]
  (ds/system :dev
    {[:env]
     (env-config :test)

     [:http :server]
     ::disabled

     [:http :middleware ::ds/config :security :anti-forgery]
     false}))
