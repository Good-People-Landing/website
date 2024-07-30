(ns gpl.api.cms
  (:require
   #_["axios/dist/axios.min.js" :as axios]
   [camel-snake-kebab.core :as csk]
   [camel-snake-kebab.extras :as cske]
   [gpl.config :refer [sanity-endpoint]]
   [cljstache.core :refer [render]]
   [clojure.string]
   [gpl.utils.axios :as axios]))

(def get-entries
  "*[_type == \"entry\"] {
  \"image\": image.asset->url,
  title
}")

(defn gallery-query
  [id]
  (str "*[_type == \"gallery\" && title == \"" id "\" && !(_id match \"drafts.*\")] {
  title,
  \"images\": images[] {
    \"url\": asset->url,
    \"fp\": hotspot {
      x,
      y
    }
  }
}"))

(defn what-query
  []
  "*[_type == 'what-section'] {
  title,
  \"whats\": whats[]-> {
    title,
    \"things\": things[] {
      name,
      copy,
      url
    }
  }
}")

(defn page-query
  [type fields]
  (render "*[_type == '{{type}}'] {
           {{fields}}
          }"
          {:type type
           :fields (clojure.string/join "," fields)}))

(defn landing-page-query
  []
  (page-query "landingPage" ["heroCopy"
                             "subTitle"
                             "backgroundVideoPlaybackId"]))

(defn about-page-query
  []
  "*[_type == 'aboutPage'] {
    aboutCopy,
    \"image\": {
    \"url\": image.asset->url,
    \"fp\": image.hotspot {
      x,
      y
    }
  }
  }")

(comment

  (csk/->kebab-case {:heyThere 1})
 ;; => :repl/exception!


;;Keep from folding
  )

(defn get-landing-page!
  [callback]
  (axios/get-request
   {:url (str sanity-endpoint (js/encodeURIComponent (landing-page-query)))}
   {:on-success (fn [result]
                  (let [raw (-> result
                                :result)]
                    (tap> raw)
                    (callback (cske/transform-keys csk/->kebab-case-keyword (-> raw first)))))
    :on-error (fn [error] (tap> {:error error}))}))

(defn get-about-page!
  [callback]
  (axios/get-request
   {:url (str sanity-endpoint (js/encodeURIComponent (about-page-query)))}
   {:on-success (fn [result]
                  (let [raw (-> result
                                :result)]
                    (callback raw)))
    :on-error (fn [error] (tap> {:error error}))}))

(defn get-gallery-images!
  [collection callback]
  (axios/get-request
   {:url (str sanity-endpoint (js/encodeURIComponent (gallery-query collection)))}
   {:on-success (fn [result]
                  (let [raw (-> result
                                :result
                                first
                                :images)
                        images (mapv (fn [image]
                                       {:src (:url image)
                                        :fp (:fp image)})
                                     raw)]
                    (callback images)))
    :on-error (fn [error] (tap> {:error error}))}))

(defn get-what-copy!
  [callback]
  (axios/get-request
   {:url (str sanity-endpoint (js/encodeURIComponent (what-query)))}
   {:on-success (fn [result]
                  (let [raw (-> result
                                :result
                                first
                                :whats)]

                    (callback raw)))
    :on-error (fn [error] (tap> {:error error}))}))


(comment

  (get-landing-page! (fn [result] (tap> result)))
  (get-about-page! (fn [result] (tap> result)))

  (axios/get-request
   {:url "https://sfsmp6cu.api.sanity.io/v2021-10-21/data/query/production?query=*%5B_type%20%3D%3D%20%22entry%22%5D%20%7B%0A%20%20%22image%22%3A%20image.asset-%3Eurl%2C%0A%20%20%20%20%0A%20%20title%20%20%0A%20%20%0A%7D%0A%0A%0A%0A%0A%0A%0A%0A"}
   {:on-success (fn [result] (tap> {:result (-> result
                                                :result)}))
    :on-error (fn [error] (tap> {:error error}))})


  (axios/get-request
   {:url (str "https://1y41jyb1.api.sanity.io/v2022-03-07/data/query/production?query=" (landing-page-query))}
   {:on-success (fn [result] (tap> {:result (-> result :result)}))
    :on-error (fn [error] (tap> {:error error}))})

  (axios/get-request
   {:url (str "https://1y41jyb1.api.sanity.io/v2022-03-07/data/query/production?query=" (about-page-query))}
   {:on-success (fn [result] (tap> {:result (-> result :result)}))
    :on-error (fn [error] (tap> {:error error}))})

  (page-query "landingPage" ["subTitle"
                             "heroCopy"])

  (page-query "landingPage" ["subTitle" "heroCopy"])

  (axios/get-request
   {:url "http://localhost:3333/v2022-03-07/data/query/production?query=*%5B_type+%3D%3D+%27landingPage%27%5D"}
   {:on-success (fn [result] (tap> {:result (-> result
                                                :result)}))
    :on-error (fn [error] (tap> {:error error}))})

  (page-query "landingPage" [])

  (axios/get-request
   {:url "https://1y41jyb1.api.sanity.io/v2022-03-07/data/query/production?query=*"}
   {:on-success (fn [result] (tap> {:result result}))
    :on-error (fn [error] (tap> {:error error}))})



  ;; => :repl/exception!


  ;; => :repl/exception!

  ;; => :repl/exception!

  ;; => :repl/exception!




;;Keep from folding
  )