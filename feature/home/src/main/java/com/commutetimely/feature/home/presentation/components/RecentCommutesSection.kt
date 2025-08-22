package com.commutetimely.feature.home.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.commutetimely.core.domain.model.Commute
import com.commutetimely.core.ui.theme.cardShape

@Composable
fun RecentCommutesSection(
    recentCommutes: List<Commute>,
    onCommuteClick: (Commute) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = cardShape,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Recent Commutes",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                recentCommutes.forEach { commute ->
                    CommuteItem(
                        commute = commute,
                        onClick = { onCommuteClick(commute) }
                    )
                }
            }
        }
    }
}
