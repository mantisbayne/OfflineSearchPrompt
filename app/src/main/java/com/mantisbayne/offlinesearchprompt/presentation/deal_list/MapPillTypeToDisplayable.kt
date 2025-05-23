package com.mantisbayne.offlinesearchprompt.presentation.deal_list

import androidx.compose.ui.graphics.Color
import com.mantisbayne.offlinesearchprompt.presentation.deal_list.model.DealPillDisplayable
import com.mantisbayne.offlinesearchprompt.presentation.deal_list.model.PillType

fun mapPillTypeToDisplayable(pillType: PillType) =
    when (pillType) {
        is PillType.HotDeal -> DealPillDisplayable("Hot Deal", Color.Red)
        is PillType.Expired -> DealPillDisplayable("Expired", Color.LightGray)
        is PillType.Other -> DealPillDisplayable(pillType.infoText, Color(0xFFFFF176))
    }