(ns plzwork.editor
  (:require
   ["@editorjs/editorjs" :default EditorJS :as e]
   [reagent.core :as r]))

(defonce editor (r/atom nil))

(defn make-editor [div-id]
  (e. div-id))

(defn content [editor]
  (.save @editor))

(defn editor-panel []
  (r/create-class 
   {:component-did-mount (fn [this] (reset! editor (make-editor (clj->js {:holder "codex" :autofocus true :placeholder "T Y P E"}))))
    :reagent-render (fn [] [:div#codex {:style {:background "red" :font "serif"}}])}))

