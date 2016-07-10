(ns bernhard.core
  (:require [goog.dom :as gdom]
            [om.core]
            [om.next :as om :refer-macros [defui]]
            [om.dom :as dom]))

(def app-state (atom {:current-tab 0}))

(defui Alerts
  Object
  (render [this]
          (dom/div nil "No alerts!")))

(defui Hosts
  Object
  (render [this]
          (dom/div nil "No hosts!")))

(defui Streams
  Object
  (render [this]
          (dom/div nil "No streams!")))

(defui Notifications
  Object
  (render [this]
          (dom/div nil "No notifications!")))

(defui Config
  Object
  (render [this]
          (dom/div nil "No config!")))

(def tabs [{:name "Alerts" :ui Alerts}
           {:name "Hosts" :ui Hosts}
           {:name "Streams" :ui Streams}
           {:name "Notifications" :ui Notifications}
           {:name "Config" :ui Config}])

(defui Tabs
  Object
  (render [this]
          (let [{:keys [current-tab]} (om/props this)]
           (dom/div #js {:id "tab-container"}
                    (dom/div
                     #js {:id "tab-bar"
                          :tabIndex 1
                          :onKeyDown (fn [e]
                                       (cond
                                         (= "ArrowUp" (.-key e))
                                         (swap! app-state update-in [:current-tab] (fn [x] (mod (dec x) (count tabs))))
                                         (= "ArrowDown" (.-key e))
                                         (swap! app-state update-in [:current-tab] (fn [x] (mod (inc x) (count tabs))))
                                         :else nil))}
                             (map-indexed
                              #(dom/div #js 
                                        {:key %1
                                         :className (if (= %1 current-tab) "selected tab" "tab")
                                         :onClick (fn [x]
                                                    (swap! app-state update-in [:current-tab] (fn [y] %1)))} (:name %2)) tabs))
                    ((om/factory (:ui (nth tabs current-tab))) (om/props this))))))

(defui Counter
  Object
  (render [this]
          (let [{:keys [count]} (om/props this)]
            (dom/div nil
                     (dom/span nil (str "Count: " count))
                     (dom/button 
                      #js {:onClick
                           (fn [e]
                             (swap! app-state update-in [:count] inc))}
                      "Click me!")))))

(def reconciler
  (om/reconciler {:state app-state}))

(om/add-root! reconciler
              Tabs (gdom/getElement "app"))

(enable-console-print!)
