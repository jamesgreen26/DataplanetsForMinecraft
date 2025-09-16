package ace.actually.dataplanets.radio;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerLevel;

import java.util.ArrayList;
import java.util.List;

/**
 * A radio is a device that is used to transmit and receive signals
 * In our case it has specific functions:
 * - to receive communication from one of several "bands"
 *  - received communication could be News, Distress or radio anomalies
 * - to transmit communication on these "bands"
 *  - transmitted communication could be requests to stations or ships
 * - all information is transmitted as "spoken" text
 * Bands are explained as such
 * - There are 100 bands of communication
 * - lower bands require less power to transmit and less complex machinery to receive
 * - higher bands can be received from a lot further away
 * - band 1 can be received 8 blocks away, and each successive band multiplies this distance by 8
 * - every quarter of the max distance, information becomes fainter/decays
 * - distances are calculated across planets and space
 * - some structures will have receivers that you can transmit to, and will respond with information
 *  - on some planets, research stations or airfields may use a low band responder to give atmospheric conditions
 *  - some responders may instead be on higher bands and send their location
 * - Different bands may be used for different things in different parts of the universe
 * - communication may be encrypted with a passphrase
 *  - anyone who has that passphrase stored will be able to see the information
 *  - a player could try and guess the passphrase, some machines mayy also be able to do this
 */
public class RadioSpec {

    private static ListTag RADIOS = new ListTag();

    /**
     * Start transmitting a message from a position on a band
     * this method also checks for static triggers from structures based on the message sent
     * @param level the planet
     * @param pos the radio transmitter's physical location
     * @param band qed
     * @param message qed
     * @return the index in RADIOS of this radio
     */
    public static int transmit(ServerLevel level, BlockPos pos, int band,String message)
    {
        for(int i = 0; i < RADIOS.size(); i++) {
            CompoundTag radio = RADIOS.getCompound(i);
            if(level.dimension().location().toString().equals(radio.getString("planet")))
            {
                int[] v = radio.getIntArray("pos");
                if(v[0]==pos.getX() && v[1]==pos.getY() && v[2]==pos.getZ())
                {
                    radio.putString("message",message);
                    RADIOS.set(i,radio);
                    checkStaticTriggers(level, pos, band, message);
                    return i;
                }
            }
        }
        CompoundTag radio = new CompoundTag();
        radio.putString("planet",level.dimension().location().toString());
        radio.putIntArray("pos",new int[]{pos.getX(),pos.getY(),pos.getZ()});
        radio.putInt("band",band);
        radio.putString("message",message);
        RADIOS.add(radio);
        checkStaticTriggers(level, pos, band, message);
        return RADIOS.size()-1;
    }

    /**
     * Allows specific messages to access previously un-set-up radio responders
     * this can be used for structures that have radios within there construction (on placement)
     * This should check as far as you would expect this band to transmit:
     * - most static triggers will be low band, and thus can just be checked locally, on that planet or station
     * - some static triggers may be medium band, these should check nearby planets and space, and should be used sparingly
     * - some static triggers may be high/ultra-high band, these should not use structure locations unless they are static
     * @param level the dimension we are checking from
     * @param pos the location we are checking from
     * @param band qed
     * @param message qed
     */
    public static void checkStaticTriggers(ServerLevel level, BlockPos pos, int band,String message)
    {
        //TODO: for specific messages, search for structures with radios that haven't been setup yet
    }

    /**
     * Scan through all radios currently transmitting and see if there message is receivable
     * @param level the planet
     * @param pos the physical location of the radio receiver;
     * @param band qed
     * @return a list of all the receivable transmissions on this band at this location
     */
    public static List<String> receive(ServerLevel level, BlockPos pos, int band)
    {
        List<String> messages = new ArrayList<>();
        for (int i = 0; i < RADIOS.size(); i++) {
            CompoundTag radio = RADIOS.getCompound(i);
            if(level.dimension().location().toString().equals(radio.getString("planet")))
            {
                int[] v = radio.getIntArray("pos");
                BlockPos radioPos = new BlockPos(v[0],v[1],v[2]);

                //we use 64 here to account for the square distance, that is, 8^2
                if(pos.distSqr(radioPos)<Math.pow(band,64))
                {
                    messages.add(radio.getString("message"));
                }
            }
            else
            {
                //TODO: Work out distance calculations for planets other than where the receiver is
            }
        }
        return messages;
    }
}
