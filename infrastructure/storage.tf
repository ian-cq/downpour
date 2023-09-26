# Makes the default Storage bucket accessible for Firebase SDKs, authentication, and Firebase Security Rules.
resource "google_firebase_storage_bucket" "default-bucket" {
  provider  = google-beta
  project   = google_project.storage.project_id
  bucket_id = google_app_engine_application.default.default_bucket
}

