package pokemon.dto;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public record ChartConfig(ChartType type, ChartData data) {
    
    /* exemple avec deux séries de données -> {
        type:'bar',
        data:{
            labels:['Q1','Q2','Q3','Q4'], 
            datasets:[
                {label:'Users',data:[50,60,70,180]},
                {label:'Revenue',data:[100,200,300,400]}
            ]
        }
    }*/

    public static enum ChartType {
        @SerializedName("bar")
        BAR
    }

    public static record ChartDataSet(String label, List<Long> data) {}

    public static record ChartData(List<String> labels, List<ChartDataSet> datasets) {}
    
}
