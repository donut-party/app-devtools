(ns donut.bakery.frontend.components.endpoint-routes
  (:require
   [donut.bakery.frontend.ui :as ui]
   [re-frame.core :as rf]))

(def http-methods
  #{:get :put :post :update :delete :options})

(defn endpoint-method-handlers
  [route-options]
  [:table {:class "border-collapse border-y min-w-full"}
   (into [:tbody]
         (mapv (fn [[method method-options]]
                 [:tr {:class "divide-x"}
                  [:td {:class "p-4 align-text-top"}
                   [:pre {:class "text-sm"} (str method)]]
                  [:td
                   [ui/pppre method-options]]])
               (select-keys route-options http-methods)))] )

(defn endpoint-route
  [[path route-options]]
  [:div {:class "bg-white mt-4 rounded-lg border-b-4"}
   [:div {:class "text-lg p-4 rounded-t-lg"}
    [:div {:class "font-mono font-bold rounded p-1"} path]
    [:div {:class "font-mono"} (str (:name route-options))]
    (when-let [{:keys [:donut.system/doc]} route-options]
      [:div {:class "italic text-gray-400 text-md"} doc])]
   ;; [ui/pppre (select-keys route-options [:id-key :ent-type])]
   [endpoint-method-handlers route-options]
   [:div {:class "p-4"}
    [:h2 {:class "mb-4"} "common options"]
    [ui/pppre (apply dissoc route-options (into http-methods
                                                [:muuntaja
                                                 :middleware
                                                 :coercion]))]]])

(defn component
  []
  (into
   [:div
    [ui/h1 "Endpoint Routes"]]
   (mapv endpoint-route @(rf/subscribe [:endpoint-routes]))))
