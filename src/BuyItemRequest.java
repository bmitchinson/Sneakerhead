public class BuyItemRequest extends Request {
    private final Item item;

    public BuyItemRequest(Item item){
        this.item = item;
    }

    public int getItem(){
        return item.getId();
    }
}
