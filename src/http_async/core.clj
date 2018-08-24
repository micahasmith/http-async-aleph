(ns http-async.core
  (:require
    [clojure.pprint :refer [pprint]]
    [ring.middleware.reload :refer [wrap-reload]]
    [ring.middleware.keyword-params :refer [wrap-keyword-params]]
    [ring.middleware.defaults :refer :all]
    [compojure.route :refer [files not-found]]
    [compojure.handler :refer [site]]                       ; form, query params decode; cookie; session, etc
    [compojure.core :refer [routes GET POST DELETE ANY context]]
    [ring.middleware.params :as params]
    [compojure.response :refer [Renderable]]
    [aleph.http :as http]
    [byte-streams :as bs]
    [manifold.stream :as s]
    [manifold.deferred :as d]
    [cheshire.core :refer :all]
    [clojure.core.async :as a]
    [clojure.java.io :refer [reader writer]])
  (:gen-class))

(extend-protocol Renderable
  manifold.deferred.IDeferred
  (render [d _] d))

(defn ex-handler [request]
  (pprint request)
  {:status      200
   :body        "hello worldy"})

(defn ex-async-handler [request]
  {:status 200
   :headers {"content-type" "text/plain"}
   :body (s/->source (a/go "hello world asyncyy"))})

(defn ex-post-handler [{:keys [body] :as request}]
  (pprint (-> body reader (parse-stream true)))
  {:status 200
   :headers {"content-type" "text/plain"}
   :body (-> "true" )})

(def app-routes
  (routes
    (GET "/" [] ex-handler)
    (GET "/async" [] ex-async-handler)
    (POST "/json" [] ex-post-handler)
    (not-found {:status 404})))

(def reloadable-app
    (wrap-defaults app-routes
                   ;; https://github.com/ring-clojure/ring-defaults
                   (assoc site-defaults
                     ;; this is going to be behind a proxy (nginx)
                     :proxy true
                     ;; don't need a session
                     :session false
                     ;; don't need cookies
                     :cookies false
                     ;; don't need anti-forgery
                     :security {:anti-forgery false}
                     :params {:keywordize true})
                   ))


(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!")
  (def server (http/start-server #'app-routes {:port 8086})))


;; to close the server
;(.close server)



