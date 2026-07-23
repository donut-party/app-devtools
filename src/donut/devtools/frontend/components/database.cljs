(ns donut.devtools.frontend.components.database
  (:require
   [donut.devtools.frontend.ui :as ui]
   [donut.frontend.form.components :as dfc]
   [donut.frontend.form.flow :as dff]
   [re-frame.core :as rf]))

(def border-style "border border-collapse border-slate-200")

(defn table
  [table-name {:keys [columns column-order]}]
  [:div
   [:div {:class "font-mono font-bold"}
    table-name]
   [:table {:class "mt-1"}
    [:tbody
     [:<> (map (fn [column-name]
                 [:tr {:key column-name}
                  [:td {:class ["p-3 font-mono italic text-sm align-top" border-style]}
                   (name column-name)]
                  [:td {:class border-style}
                   [ui/pppre (get-in columns [column-name :raw])]]])
               column-order)]]]])

(defn table-link
  [table-name]
  [:div {:class "px-2 py-1 bg-slate-300" :key table-name}
   table-name])

(defn table-name-filter
  []
  (dfc/with-form {::dff/form-key [:filter :table-name]}
    [:div {:class "mb-2"}
     [*input {:type                  :text
              :donut.input/attr-path :table-name}
      {:placeholder "filter tables"
       :class       "px-2 py-1 w-full"}]]))

(defn component
  []
  [:div
   [ui/h1 "Database"]
   (let [{:keys [table-order tables]}  @(rf/subscribe [:donut.devtools/filtered-dbxray])]
     [:div {:class "grid grid-cols-5 mt-2"}
      [:div {:class "bg-slate-200 p-4"}
       [table-name-filter]
       [:<> (->> table-order
                 sort
                 (map (fn [table-name] ^{:key table-name} [table-link table-name])))]]
      (->> table-order
           sort
           (map (fn [table-name]
                  [table table-name (table-name tables)]))
           (into [:div {:class "p-4 bg-white divide-y divide-slate-100 col-span-4"}]))])])
