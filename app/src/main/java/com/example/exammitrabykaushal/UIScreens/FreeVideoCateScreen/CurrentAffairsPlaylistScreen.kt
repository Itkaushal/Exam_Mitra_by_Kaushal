package com.example.exammitrabykaushal.UIScreens.FreeVideoCateScreen


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayCircleFilled
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ---------------------------
// DATA MODEL
// ---------------------------
data class CurrentAffairsPlaylistItem(
    val title: String,
    val duration: String,
    val topic: String
)

// ---------------------------
// MAIN SCREEN
// ---------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrentAffairsPlaylistScreen(
    onBack: () -> Unit,
    onVideoSelected: (String) -> Unit
) {

    val playlist = getDummyCurrentAffairsPlaylist()
    val BluePrimary = Color(0xFF1565C0)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("CurrentAffairs Playlist", fontSize = 18.sp)
                        Text("Chapter Wise Videos", fontSize = 12.sp)
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

        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Color(0xFFF5F5F5)),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(playlist) { item ->
                CurrentAffairsPlaylistCard(item) {
                    onVideoSelected(item.title)
                }
            }
        }
    }
}

// ---------------------------
// CARD ITEM UI
// ---------------------------
@Composable
fun CurrentAffairsPlaylistCard(
    item: CurrentAffairsPlaylistItem,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // Play Icon Box
            Surface(
                color = Color(0xFFE3F2FD),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.size(50.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        Icons.Default.PlayCircleFilled,
                        contentDescription = null,
                        tint = Color(0xFF1565C0),
                        modifier = Modifier.size(30.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.title,
                    fontSize = 16.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${item.topic}  •  ${item.duration}",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

// ---------------------------
// DUMMY PLAYLIST DATA
// ---------------------------
fun getDummyCurrentAffairsPlaylist(): List<CurrentAffairsPlaylistItem> {
    return listOf(
        CurrentAffairsPlaylistItem("Number Series Tricks", "12:30", "Number Series"),
        CurrentAffairsPlaylistItem("Blood Relation Concepts", "10:15", "Blood Relation"),
        CurrentAffairsPlaylistItem("Percentage Shortcuts", "08:42", "Percentage"),
        CurrentAffairsPlaylistItem("Profit & Loss Quick Methods", "11:20", "Profit & Loss"),
        CurrentAffairsPlaylistItem("Simple Interest Formula Trick", "09:18", "Simple Interest"),
        CurrentAffairsPlaylistItem("Compound Interest Advance", "13:45", "Compound Interest"),
        CurrentAffairsPlaylistItem("Algebra Linear Equations", "14:10", "Algebra"),
        CurrentAffairsPlaylistItem("Geometry: Triangle Basics", "15:00", "Geometry"),
        CurrentAffairsPlaylistItem("Mensuration Important Formulas", "16:25", "Mensuration"),
        CurrentAffairsPlaylistItem("Statistics Mean/Median", "09:55", "Statistics")
    )
}
