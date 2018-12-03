public class BuyItemRequest extends Request {
    private final int itemId;

    public BuyItemRequest(int itemId){
        this.itemId = itemId;
    }

    public int getItem(){
        return itemId;
    }
}
