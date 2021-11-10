import java.util.*;

public class Species {

        // add comments to the attributes for explanation
        private String ID;
        private String Provenance;
        private List<String> Scientific_Names;
        private List<String> Common_Names;
        private List<String> Labels;
        private List<String> Where_Listed;
        private List<String> Different_From_l;
        private List<String> Endemic_To_l;
        private List<String> Regions;
        private List<String> Region_Names;
        private List<String> listing_Statuses;
        //private String Conversation_Status;
        private List<String> Categories;
        private List<String> Orders;
        private List<String> Families;

        public Species() {
        }

        public Species(String ID,
                       String provenance,
                       List<String> scientific_Names,
                       List<String> common_Names,
                       List<String> labels,
                       List<String> where_Listed,
                       List<String> different_From_l,
                       List<String> endemic_To_l,
                       List<String> regions,
                       List<String> region_Names,
                       List<String> listing_Statuses,
                       //String conversation_Status,
                       List<String> categories,
                       List<String> orders,
                       List<String> families) {

                this.ID = ID;
                this.Provenance = provenance;
                this.Scientific_Names = scientific_Names;
                this.Common_Names = common_Names;
                this.Labels = labels;
                this.Where_Listed = where_Listed;
                this.Different_From_l = different_From_l;
                this.Endemic_To_l = endemic_To_l;
                this.Regions = regions;
                this.Region_Names = region_Names;
                this.listing_Statuses = listing_Statuses;
                //this.Conversation_Status = conversation_Status;
                this.Categories = categories;
                this.Orders = orders;
                this.Families = families;
        }

        public String getID() {
                return ID;
        }

        public void setID(String ID) {
                this.ID = ID;
        }

        public String getProvenance() {
                return Provenance;
        }

        public void setProvenance(String provenance) {
                this.Provenance = provenance;
        }

        public List<String> getScientific_Names() {
                return Scientific_Names;
        }

        public void setScientific_Names(List<String> scientific_Names) {
                this.Scientific_Names = scientific_Names;
        }

        public List<String> getCommon_Names() {
                return Common_Names;
        }

        public void setCommon_Names(List<String> common_Names) {
                this.Common_Names = common_Names;
        }

        public List<String> getLabels() {
                return Labels;
        }

        public void setLabels(List<String> labels) {
                this.Labels = labels;
        }

        public List<String> getWhere_Listed() {
                return Where_Listed;
        }

        public void setWhere_Listed(List<String> where_Listed) {
                this.Where_Listed = where_Listed;
        }

        public List<String> getDifferent_From_l() {
                return Different_From_l;
        }

        public void setDifferent_From_l(List<String> different_From_l) {
                this.Different_From_l = different_From_l;
        }

        public List<String> getEndemic_To_l() {
                return Endemic_To_l;
        }

        public void setEndemic_To_l(List<String> endemic_To_l) {
                this.Endemic_To_l = endemic_To_l;
        }

        public List<String> getRegions() {
                return Regions;
        }

        public void setRegions(List<String> regions) {
                this.Regions = regions;
        }

        public List<String> getRegion_Names() {
                return Region_Names;
        }

        public void setRegion_Names(List<String> region_Names) {
                this.Region_Names = region_Names;
        }

        public List<String> getListing_Statuses() {
                return listing_Statuses;
        }

        public void setListing_Statuses(List<String> listing_Statuses) {
                this.listing_Statuses = listing_Statuses;
        }


        public List<String> getCategories() {
                return Categories;
        }

        public void setCategories(List<String> categories) {
                this.Categories = categories;
        }

        public List<String> getOrders() {
                return Orders;
        }

        public void setOrders(List<String> orders) {
                this.Orders = orders;
        }

        public List<String> getFamilies() {
                return Families;
        }

        public void setFamilies(List<String> families) {
                this.Families = families;
        }
}
