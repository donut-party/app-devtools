(ns donut.bakery.frontend.ui
  (:require
   [cljs.pprint :as pprint]
   [donut.frontend.nav.components :as dnc]
   [react :as react]
   [reagent.core :as r]
   [reagent.dom :as rd]))

(defn link
  [opts & children]
  (into [dnc/simple-route-link (merge {:class "text-blue-600 hover:text-green-600 hover:bg-gray-50"} opts)]
        children))

(defn h1
  [& children]
  (into
   [:h1 {:class "order-1 text-gray-9009 text-3xl font-extrabold tracking-tight mt-2"}]
   children))

(defn h2
  [& children]
  (into
   [:h1 {:class "order-1 text-gray-9009 text-2xl font-bold tracking-tight mt-2"}]
   children))

(defn table
  [& children]
  (into
   [:table {:class ["table-auto"
                    "w-full"
                    "mx-auto"
                    "overflow-auto"
                    "divide-y"
                    "divide-slate-300"]}]
   children))

(defn td
  [& children]
  (into [:td {:class "align-top p-3"}] children))

(defn highlight
  [x]
  (let [dom (r/atom {})]
    (fn [x]
      (react/useEffect (fn [] (.highlightElement js/Prism (:code @dom))))
      [:div
       [:pre
        {:class "text-sm"}
        [:code {:class                   "language-clojure"
                :dangerouslySetInnerHTML {:__html (with-out-str (pprint/pprint x))}
                :ref                     #(swap! dom assoc :code %)}]]])))

(defn pppre
  [x]
  [:f> highlight x] )
