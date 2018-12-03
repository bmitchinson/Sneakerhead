import Requests.Request;

// extends the Serializable object Request to send a request to "sell" an item through the output stream

public class SellItemRequest extends Request {
    private Item item;

    public SellItemRequest(Item item){
        this.item = item;
    }

    public Item getItem() {
        return item;
    }

    public String getSeller(){
        return item.getSeller();
    }
}
