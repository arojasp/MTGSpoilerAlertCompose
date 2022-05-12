package es.alejandro.mtgspoileralert.sets.model

import com.google.gson.annotations.SerializedName

enum class SetType {
    @SerializedName("core")
    CORE,
    @SerializedName("expansion")
    EXPANSION,
    @SerializedName("masters")
    MASTERS,
    @SerializedName("masterpiece")
    MASTERPIECE,
    @SerializedName("from_the_vault")
    FROM_THE_VAULT,
    @SerializedName("spellbook")
    SPELLBOOK,
    @SerializedName("premium_deck")
    PREMIUM_DECK,
    @SerializedName("duel_deck")
    DUEL_DECK,
    @SerializedName("draft_innovation")
    DRAFT_INNOVATION,
    @SerializedName("treasure_chest")
    TREASURE_CHEST,
    @SerializedName("commander")
    COMMANDER,
    @SerializedName("planechase")
    PLANECHASE,
    @SerializedName("archenemy")
    ARCHENEMY,
    @SerializedName("vanguard")
    VANGUARD,
    @SerializedName("funny")
    FUNNY,
    @SerializedName("starter")
    STARTER,
    @SerializedName("box")
    BOX,
    @SerializedName("promo")
    PROMO,
    @SerializedName("token")
    TOKEN,
    @SerializedName("memorabilia")
    MEMORABILIA
}