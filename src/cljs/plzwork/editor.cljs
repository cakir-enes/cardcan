(ns plzwork.editor
  (:require
   ["@editorjs/editorjs" :default EditorJS :as ed]
   ["@editorjs/header" :default Header :as header]
   ["@editorjs/list" :default List :as lists]

   [reagent.core :as r]))

(defonce editor (r/atom nil))

(defn make-editor [div-id]
  (ed. (clj->js {:holder div-id
                 :tools {:header {:class header :inlineToolbar ["link"]}
                         :list {:class lists :inlineToolbar true}}
                 :on-ready #(js/document.body.addEventListener "keydown" (fn [] (println "KEY DOWN")))})))

(defn content [editor]
  (.save @editor))

(defn editor-panel []
  (r/create-class 
   {:component-did-mount (fn [this] (reset! editor (make-editor "codex")))
    :reagent-render (fn [] [:div [:div#codex {:style {}}]])}))

