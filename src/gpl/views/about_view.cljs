(ns gpl.views.about-view
  (:require [gpl.lib.defnc :refer [defnc]]
            [gpl.reducers.requires]
            [helix.core :refer [$]]
            ["gsap" :refer [gsap]]
            [helix.dom :as d]
            [helix.hooks :as hooks]))

(defnc about-view
  [{:keys [active
           intro-complete-callback
           outro-complete-callback]}]

  (let [comp-ref (hooks/use-ref "comp-ref")]
    (hooks/use-effect
     [active]
     (if active
       (intro-complete-callback)
       (.to gsap
            @comp-ref
            #js{:opacity 0
                :onComplete outro-complete-callback
                :duration 1})))

    (d/div {:ref comp-ref}
           (d/p "About"))))