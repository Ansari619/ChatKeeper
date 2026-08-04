# ChatKeeper

Ek simple Android app jisme do features hain:

1. **Deleted Messages** – WhatsApp ke incoming message notifications ko turant capture kar ke local database mein save kar leti hai. Agar sender message "delete for everyone" kare, saved copy app mein reh jaati hai.
2. **Status Saver** – WhatsApp status folder se photos/videos ko gallery mein save karti hai.

Theme WhatsApp jese hi (dark green / teal / light green) rakha gaya hai. Koi ads nahi hain — code mein koi ad SDK add hi nahi kiya gaya.

## APK kaise banayein (GitHub Actions se, free, koi ads nahi)

Agar aapke paas Android Studio nahi hai, to GitHub ka free "Actions" feature use karein - ye GitHub ke apne servers par build karta hai, koi third-party website nahi to koi ad bhi nahi aayegi.

1. **GitHub account** banayein (agar nahi hai): https://github.com/signup
2. GitHub par ek **naya repository** banayein (Public rakhein - free unlimited build minutes milti hain Public repos par):
   - github.com par "New repository" dabayein → naam dein (e.g. `ChatKeeper`) → Create karein
3. Is poore folder (jo maine di hai) ko us repository mein upload karein:
   - Repository page par "Add file → Upload files" dabayein
   - Poora `ChatKeeper` folder ka content (sab files/folders) drag-drop kar dein
   - Neeche "Commit changes" dabayein
4. Ab upar **"Actions"** tab par jayein
   - Agar workflow khud start na ho, to left side mein "Build APK" workflow par click karein → "Run workflow" button dabayein
5. Build chalne mein 3-5 minute lagenge (green tick ka wait karein)
6. Build complete hone ke baad, us run ko open karein → neeche **"Artifacts"** section mein `ChatKeeper-debug-apk` milegi → download kar lein (zip file hogi, andar APK hogi)
7. Wo APK phone mein bhej kar install kar lein (Settings → allow install from unknown source)

Ye tareeqa 100% free hai (public repo) aur poora build process GitHub khud karta hai - koi ad, koi malware, koi tracking nahi.

## APK kaise banayein (Android Studio se, agar available ho)

Steps:

1. **Android Studio** install karein (free): https://developer.android.com/studio
2. Is `ChatKeeper` folder ko **"Open"** karein Android Studio mein (File → Open).
3. Gradle sync khud ho jayega (internet chahiye hoga dependencies download karne ke liye).
4. **Build → Build Bundle(s)/APK(s) → Build APK(s)** dabayein.
5. APK yahan milegi: `app/build/outputs/apk/debug/app-debug.apk`
6. Ye file apne phone mein bhej kar install kar lein (Unknown Sources allow karna padega).

## Phone par app use karne ka tareeqa

**Deleted Messages on karne ke liye:**
- App kholein → "Enable Notification Access" button dabayein
- WhatsApp jaisi list mein "ChatKeeper" ko dhoondh kar ON karein
- Ab jo bhi WhatsApp message notification aayega, wo save ho jayega — chahe sender baad mein delete kare ya na kare

**Status Saver ke liye:**
- Pehle WhatsApp kholein, Status tab mein jaake un logon ke status dekh lein jo save karne hain (status "view" hona zaroori hai tabhi wo phone mein download hota hai)
- Fir ChatKeeper app mein "Select WhatsApp Status Folder" dabayein
- Jo folder khulega usmein `Android/media/com.whatsapp/WhatsApp/Media/.Statuses` tak navigate karein (ya jo bhi path pehle se khula ho) aur "Use this folder" select karein
- Statuses grid mein dikh jayenge, har ek ke corner par save button hoga

## Important note

- Ye app WhatsApp ka original code modify nahi karti, isliye account ban/logout hone ka koi risk nahi.
- Deleted messages sirf unhi ke capture honge jinki notification already aa chuki thi.
- DP (profile picture) save karne wala feature is app mein include nahi kiya gaya, kyunki WhatsApp koi official/reliable tareeqa nahi deta jisse kisi bhi contact ki DP fetch ki ja sake.
