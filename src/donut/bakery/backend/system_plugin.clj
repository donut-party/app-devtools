(ns donut.bakery.backend.system-plugin
  (:require
   [clojure.pprint]
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
        ;; so that bakery endpoints have access to started components
        :config {:top-node     (ds/ref [:http :server])
                 :system-store (ds/local-ref [:system-store])}})

(def SystemStoreComponent
  #::ds{:start system-store})

(def BakeryComponentGroup
  {:store-system StoreSystemComponent
   :system-store SystemStoreComponent})
