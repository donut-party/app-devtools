(ns donut.bakery.backend.system-plugin
  (:require
   [donut.system :as ds]))


(def system-store
  "used to convey system into endpoints"
  (atom nil))

(def StoreSystemComponent
  #::ds{:start (fn [{:keys [::ds/system ::ds/config]}]
                 (reset! (:system-store config) system)
                 nil)
        ;; TODO this could be the topsorted last component to start
        :config {:http-server (ds/ref [:http :server])
                 :system-store (ds/local-ref [:system-store])}})

(def SystemStoreComponent
  #::ds{:start system-store})

(def BakeryComponentGroup
  {:store-system StoreSystemComponent
   :system-store SystemStoreComponent})
