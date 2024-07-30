(ns gpl.components.hero-header
  (:require [gpl.api.cms]
            [gpl.api.cms :refer [get-gallery-images!] :as cms]
            [gpl.components.elements.rotating-lazy-image-gallery :refer [rotating-lazy-image-gallery]]
            [gpl.components.sections.quote-section :refer [quote-section]]
            [gpl.hooks.use-scroll-trigger :refer [use-scroll-trigger]]
            [gpl.components.fragments.about-me :refer [about-me]]
            [gpl.lib.defnc :refer [defnc]]
            [helix.core :refer [$]]
            [helix.dom :as d]
            [helix.hooks :as hooks]))

(defnc hero-header
  []
  (let [outer-ctx (hooks/use-ref "outer-ctx")
        [visited? is-active?] (use-scroll-trigger outer-ctx {:end "bottom"})
        [hero-images set-hero-images!] (hooks/use-state [])]

    (hooks/use-effect
     :once
     (get-gallery-images! "clojure-poem" set-hero-images!))

    (d/div {:id "hero"
            :ref outer-ctx
            :class "relative
                    h-screen
                    w-screen
                    overflow-hidden"}
           (d/div {:class "h-full
                           w-full
                           relative 
                           flex items-center
                           justify-items-center justify-center"}
                  (d/div {:class "z-10 absolute w-full h-full"}
                         ($ rotating-lazy-image-gallery {:images hero-images
                                                         :transition {:duration 0.2
                                                                      :opacity 1}
                                                         :should-play? is-active?
                                                         :should-load? visited?
                                                         :rate 50}))
                  (d/div {:class "z-20 absolute w-full h-full"}
                         (d/div {:class "w-full h-full absolute pink-grad opacity-30"})
                         ($ quote-section {:section-id "main-quote"
                                           :from {:opacity 0
                                                  :duration 0.5
                                                  :ease "expo.inOut",
                                                  :stagger 0.02}}
                            ($ about-me)))))))