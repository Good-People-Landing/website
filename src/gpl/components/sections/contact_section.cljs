(ns gpl.components.sections.contact-section
  (:require [helix.core :refer [$]]
            [gpl.components.sections.video-section :refer [video-section]]
            [helix.hooks :as hooks]
            [helix.dom :as d]
            ["gsap" :refer [gsap]]
            [gpl.components.elements.rotating-lazy-image-gallery :refer [rotating-lazy-image-gallery]]
            [gpl.hooks.use-scroll-trigger :refer [use-scroll-trigger]]
            ["gsap/SplitText" :refer [SplitText]]
            [gpl.api.cms :refer [get-gallery-images!] :as cms]
            [gpl.components.elements.lazy-image :refer [lazy-image]]
            [applied-science.js-interop :as j]
            [gpl.lib.defnc :refer [defnc]]))

(defn current-year []
  (let [date (js/Date.)]
    (.getFullYear date)))

(defnc contact-section [{:keys [gradient-class
                                is-visible?
                                force-on?]}]
  (let [outer-ctx (hooks/use-ref "outer-ctx")
        [background-images set-background-images!] (hooks/use-state nil)

        [visited? is-active?] (use-scroll-trigger outer-ctx)]

    (hooks/use-effect
     :once
     (get-gallery-images! "various-tech-shots" set-background-images!))

    (d/section {:ref outer-ctx
                :class "h-screen 
                    w-screen
                    flex
                    items-end
                    justify-center
                    
                    bg-slate-700
                    relative"}

               (hooks/use-memo
                [is-active? visited? background-images]
                (d/div {:class "z-10 absolute w-full h-full"}
                       (d/div {:class "w-screen h-screen relative"}
                              (d/div {:class "absolute w-full h-full"}
                                     ($ video-section {:playback-id "FYDtoH9gEiiXQtPsfDxNh7Ib300g7LYYC14uXzCpf6SY"})))
                       #_($ video-section {:playback-id "4xg96n14D7TLhM5S02g2v4kUD00gpNMpyYLNGGcyk8U3k"})
                       #_($ rotating-lazy-image-gallery {:images background-images
                                                         :transition {:duration 0.3
                                                                      :opacity 1}
                                                         :should-play? is-active?
                                                         :should-load? visited?
                                                         :rate 3000})))


               (d/div
                {:class "flex flex-col w-full h-2/5 z-20 items-center justify-center bg-black/50 backdrop-blur-sm"} ; Add items-center and justify-center here
                (d/div {:class "flex flex-col justify-between w-4/5 h-4/5"}
                       (d/div
                        {:class "
                                  font-fira-code
                                  font-light
                                  italic
                                  text-white
                                  text-md
                                  "}
                        "To light the fire, put the wood in first.")

                       (d/div
                        (d/div
                         {:class "font-fira-code
                                  font-light
                                  text-white
                                  text-2xl"}
                         "Why not reach out?")

                        (d/a
                         {:href "mailto:hello@goodpeoplelanding.com?subject=Let's%20grow%20something"
                          :target "_blank"
                          :class "font-fira-code
                      font-light
                      text-slate-300 
                      text-2xl
                      "}
                         "hello@goodpeoplelanding.com"))

                       (d/div {:class "flex"}
                              (d/span {:class "text-xs text-white font-fira-code font-light"}
                                      (str "Copyright © " (current-year) " Good People Landing, Inc."))))))))