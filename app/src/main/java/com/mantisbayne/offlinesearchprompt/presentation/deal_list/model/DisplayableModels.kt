package com.mantisbayne.offlinesearchprompt.presentation.deal_list.model

import android.os.Build
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.compose.ui.graphics.Color
import com.mantisbayne.offlinesearchprompt.R
import com.mantisbayne.offlinesearchprompt.data.GroceryItem
import com.mantisbayne.offlinesearchprompt.presentation.deal_list.mapPillTypeToDisplayable
import java.time.ZonedDateTime

data class DealDisplayable(
    val name: String,
    val price: String,
    @DrawableRes
    val image: Int,
    val description: String,
    val expiresInText: String,
    val deals: List<DealPillDisplayable>,
    val category: String,
    val isExpanded: Boolean = false
)

data class DealPillDisplayable(
    val text: String,
    val backgroundColor: Color
)

sealed class PillType {
    data object HotDeal : PillType()
    data class Other(val infoText: String = "") : PillType()
    data object Expired : PillType()
}

data class FilterItem(
    val label: String = "",
    val isSelected: Boolean = false
)

@RequiresApi(Build.VERSION_CODES.O)
fun GroceryItem.toDisplayable(): DealDisplayable {
    val expiration = ZonedDateTime.parse(expirationDate)
    val now = ZonedDateTime.now()

    val isExpired = expiration.isBefore(now)
    val isHotDeal = discountPercent >= 20
    val expiresInDays = java.time.Duration.between(now, expiration).toDays()

    val expiresInText = if (isExpired) {
        "Expired"
    } else {
        "Ends in $expiresInDays day${if (expiresInDays != 1L) "s" else ""}"
    }
    val image = R.drawable.hotdog
    val deals = mutableListOf<PillType>().apply {
        if (isExpired) {
            add(PillType.Expired)
        }
        if (isHotDeal) {
            add(PillType.HotDeal)
        }
        if (discountPercent > 0) {
            add(PillType.Other("$discountPercent% Off"))
        }
    }
    val pills = deals.map {
        mapPillTypeToDisplayable(it)
    }
    return DealDisplayable(
        name,
        price,
        image,
        description,
        expiresInText,
        pills,
        category
    )
}
