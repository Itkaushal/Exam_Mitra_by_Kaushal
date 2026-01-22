package com.example.exammitrabykaushal.UIScreens.pyqcategoryscreen



import android.os.Build
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.firebase.database.FirebaseDatabase

// 1. Data Model for a Test Paper
data class MtsChslTestPaper(
    val key: String, // firebase key
    val title: String,
    val year: String,
    val questions: Int,
    val time: String
)

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MtsChslPyqScreen(
    examName: String, // e.g., "SSC CGL"
   /* onPaperClicked: (String) -> Unit,*/
    onBack: () -> Unit
) {
    // 2. Generate Dummy Data based on the Exam Name
    val papers = getDummyMtschslPapers(examName)
    val BluePrimary = Color(0xFF1565C0)
    val context = LocalContext.current
    var selectedPdfUrl by remember { mutableStateOf<String?>(null) }


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(examName+" Previous Paper", fontWeight = FontWeight.SemiBold)
                        Text("Available Papers", style = MaterialTheme.typography.bodySmall)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BluePrimary,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Color(0xFFF5F5F5)) // Light Grey Background
        ) {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(papers) { mtspaper ->
                    MtsChslPaperItemCard(mtspaper, onClick = {
                        loadPdfFromFirebaseMts(mtspaper.key) { pdfUrl ->
                            if (pdfUrl != null) {
                                selectedPdfUrl = pdfUrl
                            } else {
                                Toast.makeText(context, "Failed to load PDF", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                    })
                }
            }
        }
    }

    // When PDF URL is loaded, show PDF screen
    selectedPdfUrl?.let { url ->
        ViewPdfScreenMts(
            pdfUrl = url,
            onBack = { selectedPdfUrl = null }
        )
    }
}

/* ----------------------------------------------------------
   FIREBASE PDF LOADER
------------------------------------------------------------ */

@RequiresApi(Build.VERSION_CODES.O)
fun loadPdfFromFirebaseMts(paperKey: String, callback: (String?) -> Unit) {

    FirebaseDatabase.getInstance()
        .getReference("SSC_MTS_PYQ")
        .child(paperKey)
        .get()
        .addOnSuccessListener { snap ->
            callback(snap.getValue(String::class.java))
        }
        .addOnFailureListener {
            callback(null)
        }
}

/* ----------------------------------------------------------
   PDF VIEWER WITH LOADER
------------------------------------------------------------ */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewPdfScreenMts(pdfUrl: String, onBack: () -> Unit) {

    var isLoading by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("MTS Previous Paper") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, null)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1565C0),
                    titleContentColor = Color.White,
                    actionIconContentColor = Color(0xFF1565C0),
                    navigationIconContentColor = Color.White,
                    subtitleContentColor = Color(0xFF1565C0)
                )
            )
        }
    ) { padding ->

        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(color = Color.White)
        ) {

            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { context ->
                    WebView(context).apply {
                        settings.javaScriptEnabled = true
                        webViewClient = object : WebViewClient() {
                            override fun onPageFinished(view: WebView?, url: String?) {
                                isLoading = false
                            }
                        }

                        loadUrl("https://docs.google.com/gview?embedded=true&url=$pdfUrl")
                    }
                }
            )

            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(48.dp),
                    color = Color.Blue,
                    strokeCap = StrokeCap.Square,
                    trackColor = Color.LightGray,
                )
            }
        }
    }
}


@Composable
fun MtsChslPaperItemCard(paper: MtsChslTestPaper, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon Box
            Surface(
                color = Color(0xFFE3F2FD),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.size(48.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Description, // Document Icon
                        contentDescription = null,
                        tint = Color(0xFF1565C0)
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Details
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = paper.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CalendarToday, null, Modifier.size(12.dp), tint = Color.Gray)
                    Text(" ${paper.year}  •  ", fontSize = 12.sp, color = Color.Gray)
                    Icon(Icons.Default.Schedule, null, Modifier.size(12.dp), tint = Color.Gray)
                    Text(" ${paper.time}", fontSize = 12.sp, color = Color.Gray)
                }
            }

            // Start Button
            Button(
                onClick = onClick,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1565C0)),
                contentPadding = PaddingValues(horizontal = 12.dp),
                modifier = Modifier.height(36.dp)
            ) {
                Text("Start", fontSize = 12.sp)
            }
        }
    }
}

// 3. Logic to return Dummy Data
fun getDummyMtschslPapers(category: String): List<MtsChslTestPaper> {
    return listOf(
        MtsChslTestPaper("mts_pyq_1", "MTS Shift 1", "18-Dec 2024", 180, "120 Min"),
        MtsChslTestPaper("mts_pyq_2", "MTS Shift 2", "20-Dec 2024", 180, "120 Min"),
        MtsChslTestPaper("mts_pyq_3", "MTS Shift 1", "10-Dec 2024", 180, "120 Min"),
        MtsChslTestPaper("mts_pyq_4", "MTS Shift 2", "05-Dec 2024", 180, "120 Min"),
        MtsChslTestPaper("mts_pyq_5", "MTS Shift 3", "01-Dec 2024", 180, "120 Min"),
        MtsChslTestPaper("mts_pyq_6", "MTS Shift 2", "26-Dec 2024", 180, "120 Min"),
        MtsChslTestPaper("mts_pyq_7", "MTS Shift 1", "08-Dec 2024", 180, "120 Min"),
        MtsChslTestPaper("mts_pyq_8", "MTS Shift 2", "09-Dec 2024", 180, "120 Min"),
        MtsChslTestPaper("mts_pyq_9", "MTS Shift 1", "10-Dec 2024", 180, "120 Min"),
        MtsChslTestPaper("mts_pyq_10", "MTS Shift 3", "21-Dec 2024", 180, "120 Min"),
        )
}