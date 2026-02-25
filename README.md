# JobSniper AI (Android Native)

Production-ready Android application designed for **fast, compliant job application prep** for DevOps/Cloud/SRE roles.

> ✅ Compliance-first: this app does **not** automate clicks/submission inside LinkedIn, does not reverse engineer LinkedIn private APIs, and does not scrape LinkedIn DOM.

## 1) What this app does

- Monitors Gmail for LinkedIn Job Alert emails (legal integration).
- Parses alerts into structured jobs.
- Filters for:
  - Bangalore location
  - DevOps/SRE/Cloud/Kubernetes relevance
  - Posted in last 10 minutes
  - Applicants < 100 (when available)
- Calls OpenAI API to generate:
  - 4 tailored resume bullets
  - 3-line recruiter message
  - screening answers
  - ~150-word cover note
- Triggers instant push notification: **"New DevOps Job - Apply Now"**
- Opens LinkedIn job URL for **manual final submission**.
- Provides copy buttons for tailored content.

## 2) Tech stack

- Kotlin (Android native)
- MVVM architecture
- Coroutines
- Retrofit + Moshi
- Room DB cache
- WorkManager background polling
- EncryptedSharedPreferences for token storage
- Dark mode UI (Material 3)

## 3) Folder structure

```text
app/src/main/java/com/jobsniper/ai/
├── data/
│   ├── local/
│   │   ├── entity/JobEntity.kt
│   │   ├── JobDao.kt
│   │   └── JobSniperDatabase.kt
│   ├── network/
│   │   ├── dto/
│   │   ├── GmailApiService.kt
│   │   └── OpenAiApiService.kt
│   └── repository/JobRepository.kt
├── di/ServiceLocator.kt
├── domain/
│   ├── JobFilter.kt
│   ├── JobModels.kt
│   ├── JobMonitorUseCase.kt
│   └── LinkedInAlertParser.kt
├── ui/
│   ├── jobs/
│   └── main/
├── util/
│   ├── NotificationHelper.kt
│   ├── ProfileConfig.kt
│   └── TokenStore.kt
├── worker/JobMonitorWorker.kt
└── JobSniperApplication.kt
```

## 4) Build & run

### Prerequisites
- Android Studio Iguana+ / Koala+
- Android SDK 34
- JDK 17

### Local run
```bash
git clone <repo>
cd <repo>
./gradlew :app:assembleDebug
```

Install with Android Studio or `adb install app/build/outputs/apk/debug/app-debug.apk`.

## 5) Gmail API integration steps

1. Open Google Cloud Console.
2. Create project and enable **Gmail API**.
3. Configure OAuth consent screen.
4. Create OAuth client credentials.
5. Obtain user access token with scope:
   - `https://www.googleapis.com/auth/gmail.readonly`
6. Save token in app securely using `TokenStore.saveGmailToken(...)`.

Recommended production pattern: move OAuth flow to backend and send short-lived tokens to app.

## 6) OpenAI API integration

1. Generate API key in OpenAI platform.
2. Save using `TokenStore.saveOpenAiToken(...)`.
3. App calls `v1/chat/completions` with fast model config (`gpt-4o-mini`, temp 0.2).
4. Tailored output is parsed into sections and persisted in Room.

## 7) Deployment guide (production)

1. Add CI pipeline (GitHub Actions): lint, unit tests, build release.
2. Configure signing (`keystore.properties`, Play App Signing).
3. Enable Crashlytics + analytics (optional).
4. Add remote config for keywords, location filters, model name.
5. Ship to internal test track, then production track.

## 8) Security and compliance

- Tokens stored in EncryptedSharedPreferences.
- No LinkedIn UI automation.
- No private LinkedIn API usage.
- No scraping LinkedIn website/app internals.
- User manually reviews and submits each application.

## 9) Optional advanced mode (roadmap)

- Referral tracker
- Applied-jobs tracker with stages
- Recruiter response tracker
- CSV export
- Shared backend for multi-device sync
- Role/domain ranking model

## 10) Speed optimization notes

- Local cache (Room) prevents duplicate processing.
- Preloaded candidate profile/skill matrix in `ProfileConfig`.
- Fast AI model configuration.
- Background worker loop for near real-time detection.

