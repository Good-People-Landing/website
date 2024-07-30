(ns gpl.views.landing-view
  (:require [gpl.components.section :refer [section]]
            [gpl.components.sections.quote-section :refer [quote-section]]
            [gpl.components.sections.video-section :refer [video-section]]
            [gpl.components.sections.what-section :refer [what-section]]
            [gpl.components.sections.contact-section :refer [contact-section]]
            [gpl.lib.defnc :refer [defnc]]
            [gpl.reducers.requires]
            [gpl.api.cms :as cms]
            [gpl.components.hero-header :refer [hero-header]]
            [gpl.components.navs.progress-menu :refer [progress-menu]]
            [gpl.components.sections.mobile-hero-section :refer [mobile-hero-section]]
            [gpl.hooks.use-media-query :refer [use-media-query]]
            [gpl.components.playful-titles :refer [playful-titles]]

            [helix.core :refer [$]]
            [helix.dom :as d]
            [helix.hooks :as hooks]))

(defnc landing-view []
  (let [container-ref (hooks/use-ref "container-ref")
        [landing-content set-landing-content!] (hooks/use-state nil)
        is-desktop? (use-media-query :md)]

    (hooks/use-effect
     :once
     (cms/get-landing-page! set-landing-content!))

    ($ :div {:ref container-ref
             :class ""}

       (d/div {:class "fixed z-20 justify-center items-center top-1/2 -translate-y-1/2 left-2"}
              ($ progress-menu {:total-sections 3}))

       (if is-desktop?
         ($ section
            {:key "video"
             :section-id "video"}
            (d/div {:class "w-screen h-screen relative"}
                   (when (-> landing-content :background-video-playback-id)
                     (d/div {:class "absolute w-full h-full"}
                            ($ video-section {:playback-id (-> landing-content :background-video-playback-id)})))
                   (d/div {:class "absolute w-full h-full pointer-events-none"}
                          ($ playful-titles))))
         ($ section
            {:key "video"
             :section-id "video"}
            (d/div {:class "w-screen h-screen relative"}
                   (d/div {:class "absolute w-full h-full"}
                          ($ video-section {:playback-id (-> landing-content :background-video-playback-id)}))))
         #_($ section
              {:key "mobile-hero"
               :section-id "mobile-hero"}
              ($ mobile-hero-section)))

       #_(when is-desktop?
           ($ section
              {:key "hero"
               :section-id "hero"}
              ($ hero-header)))

       ($ section
          {:key "about-tech"
           :section-id "about-tech"}
          ($ quote-section {:section-id "tech-quote"
                            :gradient-class "grey-grad"
                            :from {:opacity 0
                                   :duration 0.5
                                   :ease "expo.inOut",
                                   :stagger 0.01}
                            :to {:opacity 1
                                 :duration 0.1
                                 :ease "expo.inOut",
                                 :stagger 0.1}}
             (d/div {:class "text-slate-300 font-light flex justify-center h-full flex-col md:w-3/4 w-3/4 text-lg md:text-2xl"}
                    (d/p {:class "mb-8 italic"} "Welcome to Good People Landing.")
                    (d/p {:class "mb-8"}
                         (d/span {:class "font-medium text-pink-600"} ":good ")
                         "is what we want to be, to do. we do this through permaculture, education, and bringing people together.")

                    (d/p {:class " mb-8"}
                         "we do things together, because alone it's no fun."
                         (d/span {:class "font-medium text-pink-600"} " :people")
                         ", not things move us forward.")


                    (d/p {:class " mb-8"}
                         "to deal with inevitble hardtimes, land, society, personal trials, we need a place to rest and reset. We need a safe "
                         (d/span {:class "font-medium text-pink-600"} ":landing ")
                         "place that is we set new roots and grow.")

                    (d/p {:class " mb-8"}
                         "if we can grow an inch of soil, we can grow anything."))))

       #_($ section
            {:key "video-section"
             :section-id "video-section"}
            (d/div {:class "w-screen h-screen relative"}
                   (d/div {:class "absolute w-full h-full"}
                          ($ video-section {:playback-id "4xg96n14D7TLhM5S02g2v4kUD00gpNMpyYLNGGcyk8U3k"}))))

       ($ section
          {:key "main-quote"
           :section-id "main-quote"}
          ($ quote-section {:section-id "main-quote"
                            :gradient-class "orange-grad"
                            :quote ["People growing soil"
                                    "Soil growing people"]}))

       #_($ section
            {:key "doing"
             :section-id "doing"}
            ($ quote-section {:class ""
                              :gradient-class "blue-grad"
                              :section-id "doing"
                              :header "What I Love"
                              :quote ["Making immutable data move."
                                      "Making moving images that make people stop."]}))

       #_($ section
            {:key "what"
             :section-id "what"}
            ($ what-section {:class ""
                             :gradient-class "purple-grad"
                             :section-id "what"}))

       ($ section
          {:key "contact"
           :section-id "contact"}
          ($ contact-section {:force-on? false
                              :section-id "contact"})))))