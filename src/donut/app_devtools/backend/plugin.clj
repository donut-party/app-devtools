(ns donut.app-devtools.backend.plugin
  (:require
   [clojure.pprint]
   [donut.app-devtools.backend.endpoint-routes :as endpoint-routes]
   [donut.endpoint.route-group :as derg]
   [donut.system :as ds]))

;;---
;; system store requires special print handling because it has a recursive
;; reference to the whole system, which has a reference to the system store
;; itself
;;---

(def ^:private system-store-string
  (str (symbol ::system-store)))

(defrecord SystemStore [store]
  Object
  (toString [_] system-store-string)

  clojure.lang.IDeref
  (deref [_] (deref store)))

(defmethod print-method SystemStore [v w]
  (.write w system-store-string))

(defn pprint-system-store
  [& _]
  (.write *out* system-store-string))

(. clojure.pprint/simple-dispatch addMethod SystemStore pprint-system-store)


;;---
;; components
;;---

(def system-store
  "used to convey system into endpoints"
  (map->SystemStore {:store (atom nil)}))

(def StoreSystemComponent
  #::ds{:start  (fn [{:keys [::ds/system ::ds/config]}]
                  (reset! (:store (:system-store config))
                          system)
                  nil)
        ;; TODO this could be the topsorted last component to start
        ;; `:top-node` makes sure this starts after most of rest of system
        ;; so that app-devtools endpoints have access to started components
        :config {:top-node     (ds/ref [:http :server])
                 :system-store (ds/local-ref [:system-store])}})

(def SystemStoreComponent
  #::ds{:start system-store})

(def AppDevtoolsComponentGroup
  {:store-system StoreSystemComponent
   :system-store SystemStoreComponent})


(def AppDevtoolsRoutes
  (derg/route-group
   {:path-prefix ""
    :shared-opts {:datasource   (ds/ref [:db :datasource])
                  :system-store (ds/ref [:donut.app-devtools/system-store :system-store])}
    :routes      endpoint-routes/routes}))

(def app-devtools-plugin
  {:donut.system.plugin/name
   ::app-devtools-plugin

   :donut.system/doc
   "adds developer tools"

   :donut.system.plugin/system-update
   (fn [system]
     (-> system
         (assoc-in [::ds/defs :donut.app-devtools/system-store] AppDevtoolsComponentGroup)
         (assoc-in [::ds/defs :donut.app-devtools/routes] AppDevtoolsRoutes)
         (update-in [::ds/defs :routing :routes] conj (ds/ref [:donut.app-devtools/routes :route-group]))))})
