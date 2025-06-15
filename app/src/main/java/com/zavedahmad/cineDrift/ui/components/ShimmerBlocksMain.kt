package com.zavedahmad.cineDrift.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.shimmer

@Composable
fun ShimmerBlocksMain(){
    LazyRow(modifier = Modifier.shimmer()) {
        items(5){index->
            Card(modifier = Modifier.width(200.dp).height(360.dp)) {
                Column (modifier = Modifier.padding(20.dp)){
                    Box(Modifier.width(160.dp).height(240.dp).clip(RoundedCornerShape(20.dp)).background(MaterialTheme.colorScheme.inversePrimary)){}
                    Spacer(modifier = Modifier.height(20.dp))
                    Box(Modifier.width(120.dp).height(20.dp).clip(RoundedCornerShape(10.dp)).background(MaterialTheme.colorScheme.inverseSurface)){}
                }
                Spacer(modifier = Modifier.height(20.dp))
            }

            Spacer(modifier = Modifier.width(20.dp))
        }
    }
    Spacer(modifier = Modifier.height(50.dp))
}