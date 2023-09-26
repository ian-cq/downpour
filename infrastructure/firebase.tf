# Creates a new Google Cloud project.
resource "google_project" "default" {
  provider = google-beta.no_user_project_override

  name       = "Downpour - Flood Detection Mobile Application"
  project_id = "downpour-taylors-cs23"
  # Required for any service that requires the Blaze pricing plan
  # (like Firebase Authentication with GCIP)
  # billing_account = "000000-000000-000000"

  # Required for the project to display in any list of Firebase projects.
  labels = {
    "firebase" = "enabled"
  }
}

# Enables required APIs.
resource "google_project_service" "default" {
  provider = google-beta.no_user_project_override
  project  = google_project.default.project_id
  for_each = toset([
    "cloudbilling.googleapis.com",
    "cloudresourcemanager.googleapis.com",
    "firebase.googleapis.com",
    # Enabling the ServiceUsage API allows the new project to be quota checked from now on.
    "serviceusage.googleapis.com",
  ])
  service = each.key

  # Don't disable the service if the resource block is removed by accident.
  disable_on_destroy = false
}

# Enables Firebase services for the new project created above.
resource "google_firebase_project" "default" {
  provider = google-beta
  project  = google_project.default.project_id

  # Waits for the required APIs to be enabled.
  depends_on = [
    google_project_service.default
  ]
}

# Creates a Firebase Android App in the new project created above.
resource "google_firebase_android_app" "default" {
  provider = google-beta

  project      = google_project.default.project_id
  display_name = "My Awesome Android app"
  package_name = "awesome.package.name"

  # Wait for Firebase to be enabled in the Google Cloud project before creating this App.
  depends_on = [
    google_firebase_project.default,
  ]
}

resource "google_firebase_android_app" "default" {
  provider      = google-beta
  project       = "downpour-taylors-cs23"
  display_name  = "Downpour - Flood Detection Mobile Application"
  package_name  = "android.package.app"
  sha1_hashes   = ["2145bdf698b8715039bd0e83f2069bed435ac21c"]
  sha256_hashes = ["2145bdf698b8715039bd0e83f2069bed435ac21ca1b2c3d4e5f6123456789abc"]
  api_key_id    = google_apikeys_key.android.uid
}

resource "google_apikeys_key" "android" {
  provider = google-beta

  name         = "api-key"
  display_name = "Downpour - Flood Detection Mobile Application"
  project      = "downpour-taylors-cs23"

  restrictions {
    android_key_restrictions {
      allowed_applications {
        package_name     = "android.package.app"
        sha1_fingerprint = "2145bdf698b8715039bd0e83f2069bed435ac21c"
      }
    }
  }
}
