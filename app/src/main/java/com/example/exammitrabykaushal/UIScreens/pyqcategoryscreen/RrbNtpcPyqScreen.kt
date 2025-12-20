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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.firebase.database.FirebaseDatabase

/* ----------------------------------------------------------
   DATA MODEL
------------------------------------------------------------ */

data class NtpcTestPaper(
    val key: String, // firebase key
    val title: String,
    val year: String,
    val questions: Int,
    val time: String
)

/* ----------------------------------------------------------
   MAIN SCREEN
------------------------------------------------------------ */

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NtpcPyqScreen(
    examName: String,
    onBack: () -> Unit
) {
    val papers = getDummyNtpcPapers(examName)
    val context = LocalContext.current

    // Store loaded pdf URL here
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
                    containerColor = Color(0xFF1565C0),
                    titleContentColor = Color.White,
                    actionIconContentColor = Color(0xFF1565C0),
                    navigationIconContentColor = Color.White,
                    subtitleContentColor = Color(0xFF1565C0)
                )
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
        ) {

            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(papers) { paper ->
                    NtpcPaperItemCard(
                        paper,
                        onClick = {
                            loadPdfFromFirebase(paper.key) { pdfUrl ->
                                if (pdfUrl != null) {
                                    selectedPdfUrl = pdfUrl
                                } else {
                                    Toast.makeText(context, "Failed to load PDF", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    )
                }
            }
        }
    }

    // When PDF URL is loaded, show PDF screen
    selectedPdfUrl?.let { url ->
        ViewPdfScreen(
            pdfUrl = url,
            onBack = { selectedPdfUrl = null }
        )
    }
}

/* ----------------------------------------------------------
   PAPER ITEM CARD
------------------------------------------------------------ */

@Composable
fun NtpcPaperItemCard(
    paper: NtpcTestPaper,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {

        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Surface(
                color = Color(0xFFE3F2FD),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.size(48.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Description,
                        contentDescription = null,
                        tint = Color(0xFF1565C0)
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

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

            Button(
                onClick = onClick,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Cyan.copy(alpha = 0.4f), contentColor = Color.Black),
                contentPadding = PaddingValues(horizontal = 12.dp),
                modifier = Modifier.height(36.dp)
            ) {
                Text("Start", fontSize = 14.sp)
            }
        }
    }
}

/* ----------------------------------------------------------
   FIREBASE PDF LOADER
------------------------------------------------------------ */

@RequiresApi(Build.VERSION_CODES.O)
fun loadPdfFromFirebase(paperKey: String, callback: (String?) -> Unit) {

    FirebaseDatabase.getInstance()
        .getReference("RRB_NTPC_PYQ")
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
fun ViewPdfScreen(pdfUrl: String, onBack: () -> Unit) {

    var isLoading by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("NTPC Previous Paper") },
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
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

/* ----------------------------------------------------------
   DUMMY DATA
------------------------------------------------------------ */

fun getDummyNtpcPapers(category: String): List<NtpcTestPaper> {
    return listOf(
        NtpcTestPaper("ntpc_pyq_1", "NTPC CBT 1 Shift 1", "05-06-2025", 100, "120 Min"),
        NtpcTestPaper("ntpc_pyq_2", "NTPC CBT 1 Shift 2", "10-06-2025", 100, "120 Min"),
        NtpcTestPaper("ntpc_pyq_3", "NTPC CBT 1 Shift 2", "05-06-2025", 100, "120 Min"),
        NtpcTestPaper("ntpc_pyq_4", "NTPC CBT 1 Shift 3", "05-06-2025", 100, "120 Min"),
        NtpcTestPaper("ntpc_pyq_5", "NTPC CBT 1 Shift 1", "06-06-2025", 100, "120 Min"),
        NtpcTestPaper("ntpc_pyq_6", "NTPC CBT 1 Shift 2", "06-06-2025", 100, "120 Min"),
        NtpcTestPaper("ntpc_pyq_7", "NTPC CBT 1 Shift 3", "06-06-2025", 100, "120 Min"),
        NtpcTestPaper("ntpc_pyq_8", "NTPC CBT 1 Shift 1", "09-06-2025", 100, "120 Min"),
        NtpcTestPaper("ntpc_pyq_9", "NTPC CBT 1 Shift 2", "09-06-2025", 100, "120 Min"),
        NtpcTestPaper("ntpc_pyq_10", "NTPC CBT 1 Shift 1", "10-06-2025", 100, "120 Min")
    )
}
