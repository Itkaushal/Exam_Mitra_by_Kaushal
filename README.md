📘 Exam Mitra – Smart Exam Preparation App

Exam Mitra is a modern Android exam-preparation app designed to help students practice, analyze, and improve their performance for competitive exams.
The app combines mock tests, analytics, progress tracking, and smart UI using the latest Android technologies.

🚀 Features
📝 Test & Practice

Full-length mock tests

Subject-wise tests (Math, Reasoning, GS, PYQs)

Class 9–12 academic practice

Auto-timer with pause/resume

Question navigator (bottom sheet)

📊 Performance Analytics

Accuracy trend line chart

Overall accuracy pie chart

Test-wise score history

Best score highlighting

Streak tracking (daily practice)

👤 Profile & Progress

Accuracy percentage

Practice streak

Rank estimation

Mini performance chart

Test history with swipe-to-delete

🔔 Smart Notifications

Weekly performance summary

Practice reminders

Motivation alerts

☁ Backend & Storage

Firebase Realtime Database (questions)

Room Database (test history)

Offline-first data handling

🛠 Tech Stack
🧩 Architecture

MVVM (Model–View–ViewModel)

Clean separation of UI, logic & data

🎨 UI

Jetpack Compose

Material 3 design

Dark mode ready

Smooth animations

📦 Libraries & Tools

Room – local database

Firebase – questions & auth

MPAndroidChart – performance charts

Navigation Compose

Kotlin Coroutines & Flow

ViewModel + StateFlow

com.example.exammitrabykaushal
│
├── DataLayer
│   ├── Entity
│   ├── Dao
│   └── Database
│
├── ViewModel
│   ├── DashboardViewModel
│   ├── QuizViewModel
│   └── TestHistoryViewModel
│
├── UIScreens
│   ├── screen
│   ├── component
│   └── chart_screen
│
├── navigation
│   └── AppNavGraph
│
└── repository

📊 Performance Tracking Logic

Accuracy = (Correct Answers / Total Questions) × 100

Streak = continuous daily practice

Rank calculated dynamically based on accuracy

All attempts stored in Room database

🔐 Permissions Used

Internet access (Firebase)

Notifications (weekly performance)

Local storage (Room DB)

📱 Screenshots

![SignUp](https://github.com/user-attachments/assets/9795455e-6f48-4f43-aaa8-a00104728303) ![SignIn](https://github.com/user-attachments/assets/0b26f27d-74d7-4336-9501-e3cdb43e095e)
![GoogleAuth](https://github.com/user-attachments/assets/2cefee7b-9652-4af9-93ff-b23e301b25a8) ![Dashboard](https://github.com/user-attachments/assets/4aeb932f-06ea-40db-b733-c32c2644cc9e) ![MockTest](https://github.com/user-attachments/assets/0cc3b269-eb5a-4c3d-a7f6-e1f90f20897f) ![QuestionNavigator](https://github.com/user-attachments/assets/ca2a4bbf-1fef-4dbe-ba86-c7f98f68ea4e) ![TestHistory](https://github.com/user-attachments/assets/14c963e9-aa1b-40a1-98b4-7bb6c6df64f0) ![UserPerformance](https://github.com/user-attachments/assets/9fbc1515-573d-467f-9634-3e91cc0a1396) ![PyqScreen](https://github.com/user-attachments/assets/3e77f678-4591-41b7-b0bd-5d4689e3296d) ![PyqRRBJE](https://github.com/user-attachments/assets/6a87d131-f04e-4859-93d6-41f5cf3d499d) ![Profile](https://github.com/user-attachments/assets/7be3a1ad-f05a-4ac7-987f-fc2f8dda2906)

📦 Setup Instructions

Clone the repository
git clone https://github.com/Itkaushal/Exam_Mitra_by_Kaushal.git
Open in Android Studio

Add google-services.json

Sync Gradle

Run on emulator or device

🧪 Future Enhancements

AI-based performance suggestions

Personalized study plans

Cloud sync for history

Leaderboards

PDF notes & video integration

👨‍💻 Developer

Kaushal Prajapati
Android Developer | Kotlin | Jetpack Compose
📧 Contact: kaushalprajapati9953@gmail.com

⭐ Support

If you like this project:

⭐ Star the repo

🐞 Report bugs

💡 Suggest features

🎯 Exam Mitra – Practice Smart. Improve Faster.

If you want, I can also:

Optimize this for Play Store description

Create feature graphics text

Write privacy policy

Generate app architecture diagram

Just tell me 👍
