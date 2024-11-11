package store.view;

import static store.message.OutputMessage.FREE_PRODUCT_OFFER_MESSAGE;
import static store.message.OutputMessage.ORDER_INSTRUCTION_MESSAGE;
import static store.message.OutputMessage.PROMOTION_CONDITION_NOT_MET_MESSAGE;
import static store.message.OutputMessage.STORE_OVERVIEW_MESSAGE;

import java.util.List;
import store.domain.Product;
import store.message.OutputMessage;

public class OutputView {
    public void showStoreOverview(List<Product> products) {
        displayWelcomeMessage();
        displayProductList(products);
    }

    public void displayWelcomeMessage() {
        System.out.println(STORE_OVERVIEW_MESSAGE.getMessage());
    }

    public void displayProductList(List<Product> products) {
        System.out.println();
        StringBuilder sb = new StringBuilder();
        for (Product product : products) {
            sb.append(product.toString().trim()).append("\n");
        }
        System.out.println(sb);
    }

    public void askProductNameAndQuantity() {
        System.out.println(ORDER_INSTRUCTION_MESSAGE.getMessage());
    }

    public void askAdditionalProduct(String productName, int get) {
        System.out.println();
        System.out.printf(FREE_PRODUCT_OFFER_MESSAGE.getMessage(), productName, get);
    }

    public void askForUnmetPromotion(String productName, int get) {
        System.out.println();
        System.out.printf(PROMOTION_CONDITION_NOT_MET_MESSAGE.getMessage(), productName, get);
    }

    public void askForMembershipDiscount() {
        System.out.println();
        System.out.println(OutputMessage.MEMBERSHIP_DISCOUNT_CONFIRMATION_MESSAGE.getMessage());
    }

    public void displayThankYouMessageForPurchase() {
        System.out.println();
        System.out.println(OutputMessage.PRODUCT_PURCHASE_THANK_YOU_MESSAGE.getMessage());
    }

    public void displayReceipt(String receiptContent) {
        System.out.println(receiptContent);
    }
}
