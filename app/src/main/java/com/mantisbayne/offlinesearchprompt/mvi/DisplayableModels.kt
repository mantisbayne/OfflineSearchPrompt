package com.mantisbayne.offlinesearchprompt.mvi

import android.os.Build
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import com.mantisbayne.offlinesearchprompt.R
import com.mantisbayne.offlinesearchprompt.data.GroceryItem
import java.time.ZonedDateTime

data class GroceryDisplayable(
    val name: String,
    val price: String,
    @DrawableRes
    val image: Int,
    val description: String,
    val expiresInText: String,
    val deals: List<DealPillDisplayable>,
    val category: String
)

data class DealPillDisplayable(
    val pillType: PillType
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
fun GroceryItem.toDisplayable(): GroceryDisplayable {
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
    val deals = listOf(
        DealPillDisplayable(PillType.HotDeal),
        DealPillDisplayable(PillType.Expired),
        DealPillDisplayable(PillType.Other("$discountPercent% Off"))
    )
    return GroceryDisplayable(
        name,
        price,
        image,
        description,
        expiresInText,
        deals,
        category
    )
}
