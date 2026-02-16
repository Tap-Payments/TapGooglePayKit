package company.tap.google.pay.open.enums

import com.google.android.gms.wallet.button.ButtonConstants

enum class GooglePayButtonType(val googleType: Int) {

    NORMAL_GOOGLE_PAY(ButtonConstants.ButtonType.PLAIN),
    BUY_WITH_GOOGLE_PAY(ButtonConstants.ButtonType.BUY),
    DONATE_WITH_GOOGLE_PAY(ButtonConstants.ButtonType.DONATE),
    PAY_WITH_GOOGLE_PAY(ButtonConstants.ButtonType.PAY),
    SUBSCRIBE_WITH_GOOGLE_PAY(ButtonConstants.ButtonType.SUBSCRIBE),
    CHECKOUT_WITH_GOOGLE_PAY(ButtonConstants.ButtonType.CHECKOUT),
    ORDER_WITH_GOOGLE_PAY(ButtonConstants.ButtonType.ORDER),
    BOOK_WITH_GOOGLE_PAY(ButtonConstants.ButtonType.BOOK)
}
