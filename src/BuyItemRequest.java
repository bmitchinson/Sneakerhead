public class BuyItemRequest extends Requests.Request {

    private final Item item;

    public BuyItemRequest(Item item){
        this.item = item;
    }

    public int getItemId(){
        return item.getId();
    }

    public Item getItem() {
        return item;
    }
}
