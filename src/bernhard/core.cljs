(ns bernhard.core
  (:require [goog.dom :as gdom]
            [om.core]
            [om.next :as om :refer-macros [defui]]
            [om.dom :as dom]))

(def app-state (atom {:current-tab 0
                      :alerts [{:id 1 :title "Foo is DOWN"}]
                      :hosts [{:id "i-abcdef0123" :app "foo" :stack "dev"}]
                      :streams []
                      :notifications [] 
                      :config {}}))

(defui Alerts
  Object
  (render [this]
   (dom/div #js {:id "alerts"}
            (let [{:keys [alerts]} (om/props this)]
              (if (empty? alerts)
                "No alerts. Happy day!"
                (map #(dom/div #js {:className "alert"
                                    :key (:id %)}
                               (str %)) alerts))))))

(defui Hosts
  Object
  (render [this]
   (dom/div #js {:id "hosts"}
            (let [{:keys [hosts]} (om/props this)]
              (if (empty? hosts)
                "No hosts. Nothing reporting?"
                (map #(dom/div #js {:className "host"
                                    :key (:id %)}
                               (str %)) hosts))))))

(defui Streams
  Object
  (render [this]
   (dom/div #js {:id "streams"}
            (let [{:keys [streams]} (om/props this)]
              (if (empty? streams)
                "No streams. Create one, perhaps?"
                (map #(dom/div #js {:className "stream"
                                    :key (:id %)}
                               (str %)) streams))))))

(defui Notifications
  Object
  (render [this]
   (dom/div #js {:id "notifications"} "No notifications!")))

(defui Config
  Object
  (render [this]
   (dom/div #js {:id "config"} "No config!")))

(def tabs [{:name "Alerts" :ui Alerts}
           {:name "Hosts" :ui Hosts}
           {:name "Streams" :ui Streams}
           {:name "Notifications" :ui Notifications}
           {:name "Config" :ui Config}])

(defui Tabs
  Object
  (render [this]
          (let [{:keys [current-tab]} (om/props this)]
            (dom/div
             #js {:id "tab-container"}
             (dom/div
              #js {:id "tab-bar"
                   :tabIndex 1
                   :onKeyDown (fn [e]
                                (cond
                                  (= "ArrowUp" (.-key e))
                                  (swap! app-state
                                         update-in
                                         [:current-tab]
                                         (fn [x] (mod (dec x) (count tabs))))
                                  (= "ArrowDown" (.-key e))
                                  (swap! app-state
                                         update-in
                                         [:current-tab]
                                         (fn [x] (mod (inc x) (count tabs))))
                                  :else nil))}
              (map-indexed
               #(dom/div #js 
                         {:key %1
                          :className (if (= %1 current-tab) "selected tab" "tab")
                          :onClick (fn [x]
                                     (swap! app-state update-in [:current-tab] (fn [y] %1)))} (:name %2)) tabs))
             ((om/factory (:ui (nth tabs current-tab))) (om/props this))))))

(def reconciler
  (om/reconciler {:state app-state}))

(om/add-root! reconciler
              Tabs (gdom/getElement "app"))

(enable-console-print!)
