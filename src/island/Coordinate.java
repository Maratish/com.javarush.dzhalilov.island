package island;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.beans.ConstructorProperties;

@Data
@AllArgsConstructor
@ToString
public class Coordinate {
    private int x;
    private int y;
}
