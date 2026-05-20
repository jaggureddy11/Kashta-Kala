# 🪵 Kashta-Kala

### Digital Design Catalog & Estimation System for Local Carpenters

![Android](https://img.shields.io/badge/Platform-Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![Kotlin](https://img.shields.io/badge/Language-Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)
![Room DB](https://img.shields.io/badge/Database-Room%20DB-FF6F00?style=for-the-badge&logo=sqlite&logoColor=white)
![MVVM](https://img.shields.io/badge/Architecture-MVVM-0288D1?style=for-the-badge)
![Status](https://img.shields.io/badge/Status-Completed-success?style=for-the-badge)

> **MindMatrix VTU Internship Program — Android Development Project**  
> Empowering local carpenters with digital tools for modern furniture business management.

---

# 📖 About the Project

**Kashta-Kala** is an Android application designed to modernize traditional carpentry businesses by providing a digital platform for local carpenters and furniture makers to showcase furniture designs, estimate material requirements, and generate professional quotations.

In many small towns and villages, carpenters still depend on paper catalogs and manual calculations, which often leads to:

- Poor customer visualization
- Inaccurate material estimation
- Pricing confusion
- Difficulty managing quotations
- Lack of professional presentation
- Challenges in maintaining customer records

Kashta-Kala solves these problems using a modern Android application with an offline-first approach, helping artisans improve productivity and compete with larger furniture businesses using digital technology.

---

# ✨ Features

| Feature | Description |
|---|---|
| 🖼️ **Digital Design Catalog** | Browse furniture designs including beds, sofas, tables, and cabinets |
| 📐 **Material Estimator** | Calculate required wood quantity based on dimensions |
| 💰 **Quotation Generator** | Generate detailed material and labor cost quotations |
| 📁 **Artisan Portfolio** | Showcase completed furniture works with images |
| ⭐ **Favorites System** | Save shortlisted furniture designs |
| 🗂️ **Quote History** | Store and retrieve previous customer quotations |
| 📴 **Offline First** | Core functionalities work without internet connectivity |
| ⚡ **Real-Time Calculations** | Instant estimation and quotation updates |

---

# 🛠️ Tech Stack

| Layer | Technology |
|---|---|
| **Language** | Kotlin |
| **Architecture** | MVVM (Model-View-ViewModel) |
| **UI Design** | XML Layouts, RecyclerView, CardView, Material Design |
| **Local Database** | Room Database |
| **Asynchronous Operations** | Kotlin Coroutines |
| **Image Loading** | Glide |
| **Session Management** | SharedPreferences |
| **Version Control** | Git & GitHub |
| **IDE** | Android Studio |

---

# 🗂️ Project Structure

```text
kashta-kala/
│
├── app/
│   └── src/main/
│       ├── java/com/kashtakala/
│       │   ├── ui/
│       │   │   ├── catalog/
│       │   │   ├── estimator/
│       │   │   ├── quotation/
│       │   │   └── portfolio/
│       │   │
│       │   ├── viewmodel/
│       │   ├── repository/
│       │   ├── database/
│       │   ├── models/
│       │   └── utils/
│       │
│       └── res/
│           ├── layout/
│           ├── drawable/
│           └── values/
│
├── screenshots/
├── README.md
├── build.gradle.kts
├── settings.gradle.kts
└── gradle.properties
```

---

# 📱 Application Screens

- **Home Dashboard** — Featured furniture designs and quick navigation
- **Furniture Catalog** — Browse multiple furniture categories
- **Design Details** — View dimensions and wood suggestions
- **Material Estimator** — Calculate wood requirements instantly
- **Quotation Screen** — Generate customer quotations
- **Quote History** — View and manage saved quotations
- **Portfolio Gallery** — Showcase completed works
- **Favorites Screen** — Access shortlisted furniture designs

---

# 📸 Screenshots

## 🏠 Home Dashboard



---

## 🪑 Furniture Catalog



---

## 📐 Material Estimator



---

## 💰 Quotation Generator



---

# 🚀 Getting Started

## 📋 Prerequisites

- Android Studio Hedgehog or later
- Minimum SDK: API 26+
- Kotlin 1.9+

---

# ⚙️ Installation

```bash
# Clone the repository
git clone https://github.com/your-username/kashta-kala.git
```

---

# ▶️ Open the Project

1. Open Android Studio
2. Select **Open Existing Project**
3. Choose the `kashta-kala` folder

---

# ▶️ Build & Run

1. Wait for Gradle Sync to complete
2. Connect an Android device or launch an emulator
3. Click the **Run ▶️** button

---

# 📐 Material Estimation Logic

The estimator module calculates wood requirements using area and volume formulas.

```kotlin
// Area calculation
val areaSqFt = (lengthInches * widthInches) / 144.0

// Volume calculation
val volumeCuFt = (lengthInches * widthInches * thicknessInches) / 1728.0

// Cost calculation
val totalCost = materialCost + laborCost
```

### Supported Wood Types

- Teak
- Rosewood
- MDF
- Plywood
- Pine

---

# 🧠 Core Functionalities

## 🖼️ Digital Design Catalog

Provides a visual gallery of furniture designs using RecyclerView and CardView layouts.

---

## 📐 Material Estimation

Calculates wood quantity and estimated pricing based on furniture dimensions.

---

## 💰 Quotation Generator

Creates professional customer quotations including:

- Material cost
- Labor charges
- Total estimated cost

---

## 🗄️ Offline Data Storage

Room Database ensures:

- Reliable local storage
- Fast data access
- Offline functionality
- Persistent quotation history

---

# 🏗️ Architecture Overview

```text
UI Layer (Activities / Fragments)
                ↕
         ViewModel Layer
                ↕
        Repository Layer
                ↕
       Room Database Layer
```

---

# 📅 Internship Weekly Progress

| Week | Work Done |
|---|---|
| Week 1 | Android Studio setup, Kotlin basics, XML layout design |
| Week 2 | RecyclerView and CardView implementation |
| Week 3 | MVVM architecture and Room Database integration |
| Week 4 | Furniture catalog and estimator module development |
| Week 5 | Quotation generator and portfolio implementation |
| Week 6 | UI improvements, testing, and documentation |

---

# 🚧 Challenges Faced

- Managing offline data synchronization
- Implementing accurate material calculations
- Designing responsive UI layouts
- Organizing project structure using MVVM architecture
- Optimizing RecyclerView performance for large catalogs

---

# 📚 Learning Outcomes

During the development of this project, the following skills were improved:

- Android application development using Kotlin
- MVVM architecture implementation
- Room Database integration
- Material Design principles
- Git & GitHub version control
- UI/UX design practices
- Problem-solving and debugging skills

---

# 🔮 Future Enhancements

- AI-based furniture recommendation system
- AR-based furniture preview
- Cloud backup and synchronization
- Multi-language support
- Online customer order management
- Voice-based furniture search

---

# 🌱 Impact Goals

- **Support Local Artisans** — Empower carpenters with digital business tools
- **Reduce Material Wastage** — Accurate estimation improves efficiency
- **Professional Business Management** — Generate modern quotations digitally
- **Digital Transformation** — Bring local carpentry businesses into the digital era

---

# 📋 Success Criteria

- [x] Material estimation works accurately
- [x] Furniture catalog displays correctly
- [x] Quotations generate successfully
- [x] Room Database stores quotation history
- [x] Application works offline
- [x] Portfolio gallery supports image display

---

# 👨‍💻 Developer

## Meghana S

B.E. Computer Science & Engineering(iot)  
Sir M. Visvesvaraya Institute of Technology, Bengaluru  

**MindMatrix VTU Internship Program — 2026**

---

# 📄 License

This project is developed for academic and internship evaluation purposes only.

---

# ⭐ GitHub Repository Checklist

- [x] Complete source code uploaded
- [x] Proper README documentation added
- [x] Clean folder structure maintained
- [x] Meaningful Git commit history created
- [x] Screenshots included
- [x] Build-ready Android project uploaded
- [x] Dependency files included

---




---

<p align="center">
Made with ❤️ for local artisans and carpenters
</p>
