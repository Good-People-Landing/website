(ns gpl.core

  (:require ["gsap" :refer [gsap]]
            ["gsap/ScrollToPlugin" :refer [ScrollToPlugin]]
            ["gsap/ScrollTrigger" :refer [ScrollTrigger]]
            ["gsap/SplitText" :refer [SplitText]]
            ["react-dom/client" :as rdom]
            [gpl.components.navs.logo-nav :refer [logo-nav]]
            [gpl.components.navs.side-nav :refer [side-nav]]
            [gpl.components.section-transitioner :refer [section-transitioner]]
            [gpl.lib.defnc :refer [defnc]]
            [gpl.providers.main-provider :refer [MainProvider]]
            [gpl.reducers.requires]
            [gpl.services.router :refer [router]]
            [helix.core :refer [$]]
            [mount.core :as mount]))

(defnc app []
  ($ MainProvider {:default-state {:current-section "hero"
                                   :current-subsection "start"}}
     ($ router
        ($ logo-nav)
        #_($ side-nav)
        ($ section-transitioner))))

(defonce root (rdom/createRoot (js/document.getElementById "app")))

(defn start
  []
  (tap> "Starting app")
  ;; Register all gsap plugins
  (.registerPlugin gsap ScrollToPlugin)
  (.registerPlugin gsap ScrollTrigger)
  (.registerPlugin gsap SplitText)

  (.render root ($ app)))

(defn init!
  []
  (mount/start)
  (start))
