package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.Complaint
import com.example.ui.components.ComplaintCard
import com.example.ui.theme.SchoolBluePrimary
import com.example.ui.viewmodel.ComplaintFilter

@Composable
fun ComplaintsScreen(
    complaints: List<Complaint>,
    filter: ComplaintFilter,
    onSearchChange: (String) -> Unit,
    onCategoryChange: (String) -> Unit,
    onStatusChange: (String) -> Unit,
    onNewComplaintClick: () -> Unit,
    onComplaintClick: (Complaint) -> Unit,
    onStatusClick: (Complaint) -> Unit,
    modifier: Modifier = Modifier
) {
    val categories = listOf(
        "All",
        "Academic Performance",
        "Classroom Misbehavior",
        "Attendance & Punctuality",
        "Incomplete Homework",
        "Discipline & Bullying",
        "Uniform & Grooming"
    )

    val statuses = listOf("All", "Pending", "Critical", "Resolved")

    Scaffold(
        modifier = modifier.testTag("complaints_screen"),
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onNewComplaintClick,
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                text = { Text("Write Complaint") },
                containerColor = SchoolBluePrimary,
                contentColor = Color.White,
                modifier = Modifier.testTag("complaints_fab")
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Top Bar
            Surface(
                color = Color.White,
                tonalElevation = 2.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Teacher-Parent Complaints Hub",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = SchoolBluePrimary
                        )
                    )
                    Text(
                        text = "Official student conduct & academic performance records",
                        style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF64748B))
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Search input
                    OutlinedTextField(
                        value = filter.searchQuery,
                        onValueChange = onSearchChange,
                        placeholder = { Text("Search by student, roll no, or teacher...") },
                        leadingIcon = {
                            Icon(Icons.Default.Search, contentDescription = null, tint = Color(0xFF94A3B8))
                        },
                        trailingIcon = {
                            if (filter.searchQuery.isNotEmpty()) {
                                IconButton(onClick = { onSearchChange("") }) {
                                    Icon(Icons.Default.Clear, contentDescription = "Clear")
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("complaint_search_field"),
                        shape = RoundedCornerShape(10.dp),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Status Chips
                    Row(
                        modifier = Modifier.horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        statuses.forEach { st ->
                            FilterChip(
                                selected = filter.status == st,
                                onClick = { onStatusChange(st) },
                                label = { Text(st, fontSize = 12.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = SchoolBluePrimary,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // Category Chips
                    Row(
                        modifier = Modifier.horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        categories.forEach { cat ->
                            FilterChip(
                                selected = filter.category.equals(cat, ignoreCase = true),
                                onClick = { onCategoryChange(cat) },
                                label = { Text(cat, fontSize = 11.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = Color(0xFF334155),
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }
                }
            }

            // Complaints list
            if (complaints.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "No matching complaints found",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Try clearing filters or search keyword, or tap 'Write Complaint' to create a new one.",
                            style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF64748B)),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 90.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(complaints, key = { it.id }) { complaint ->
                        ComplaintCard(
                            complaint = complaint,
                            onCardClick = { onComplaintClick(complaint) },
                            onStatusClick = { onStatusClick(complaint) }
                        )
                    }
                }
            }
        }
    }
}
