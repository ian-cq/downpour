# Enables Firebase services for the new project created above.
resource "google_firebase_project" "firestore" {
  provider = google-beta
  project  = google_project.firestore.project_id
}

# Provisions the Firestore database instance.
resource "google_firestore_database" "firestore" {
  provider = google-beta
  project  = google_project.firestore.project_id
  name     = "(default)"
  # See available locations: https://firebase.google.com/docs/projects/locations#default-cloud-location
  location_id = "name-of-region"
  # "FIRESTORE_NATIVE" is required to use Firestore with Firebase SDKs, authentication, and Firebase Security Rules.
  type             = "FIRESTORE_NATIVE"
  concurrency_mode = "OPTIMISTIC"

  # Wait for Firebase to be enabled in the Google Cloud project before initializing Firestore.
  depends_on = [
    google_firebase_project.firestore,
  ]
}


