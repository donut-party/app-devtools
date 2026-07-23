(ns donut.app-devtools.frontend.components.system
  (:require
   [donut.app-devtools.frontend.ui :as ui]
   [donut.system :as ds]
   [re-frame.core :as rf]))

(defn component-deps
  [group-name component-name topsort-graph]
  (when-let [deps (get-in topsort-graph [:adj [group-name component-name]])]
    (into [:div {:class "text-sm mb-4"}
           [:div "references"]]
          (mapv (fn [dep]
                  [:pre
                   {:class "text-sky-600"}
                   [:a {:href (str "#" dep)}
                    (str dep)]])
                deps))))

(defn component-docs
  [docs]
  (when docs
    [:div
     {:class "mt-4 mr-8 text-gray-600"}
     docs]))

(defn system-component-defs
  [group-name component-defs topsort-graph]
  [:table {:class "border-collapse border-y min-w-full"}
   (into [:tbody {:class "divide-y"}]
         (mapv (fn [[component-name component-def]]
                 [:tr
                  [:td {:id (str [group-name component-name])
                        :class "p-4 align-text-top text-sm"}
                   [:pre {:class "text-sm"} (str component-name)]]
                  [:td
                   [component-docs (::ds/doc component-def)]
                   [:div
                    {:class "rounded-lg py-4 pr-4"}
                    [ui/pppre (cond-> component-def
                                (::ds/doc component-def) (dissoc ::ds/doc))]]
                   [component-deps group-name component-name topsort-graph]]])
               component-defs))])

(defn system-component-group
  [[group-name component-defs] topsort-graph]
  [:div {:class "bg-white mt-4 rounded-lg border-b-4"}
   [:div {:class "font-bold p-4 rounded-t-lg"}
    [:pre (str group-name)]]
   [system-component-defs group-name  component-defs topsort-graph]])

(defn component
  []
  [:div
   [ui/h1 "System"]
   (into
    [:div]
    (let [{:keys [::ds/defs ::ds/graphs]} @(rf/subscribe [:donut.app-devtools/system])]
      (mapv (fn [component-group]
              (system-component-group component-group (:topsort graphs)))
            defs)))])
