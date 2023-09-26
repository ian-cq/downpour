# Provisions the default Realtime Database default instance.
resource "google_firebase_database_instance" "database" {
  provider = google-beta
  project  = google_project.rtdb.project_id
  # See available locations: https://firebase.google.com/docs/projects/locations#rtdb-locations
  region = "name-of-region"
  # This value will become the first segment of the database's URL.
  instance_id = "${google_project.rtdb.project_id}-default-rtdb"
  type        = "DEFAULT_DATABASE"

  # Wait for Firebase to be enabled in the Google Cloud project before initializing Realtime Database.
  depends_on = [
    google_firebase_project.rtdb,
  ]
}

