<?php
require_once("DBClass.php");
$db = new DB();
$db->_construct();

if (isset($_POST['ic_input'])) {
    $text = $_POST['ic_input'];

    if ($text == "") {
        return false;
    }
    else if (!is_numeric($text)) {
            echo "IC moze obsahovat len cisla!";
            return false;
        }
        else if (strlen($text) < 8 ) {
                echo "Text je prilis kratky";
                return false;
            }
            elseif (strlen($text) > 8 ) {
                echo "Text je prilis dlhy";
                return false;
            }
            else {
                $db->testForExistingIC('');
                return true;
            }

}
?>