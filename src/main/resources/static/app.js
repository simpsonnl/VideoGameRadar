function filterChecked(genre){
  if(genre.checked){
    alert("checked")
  }else{
    alert(genre.name)
  }
}

function myFunction() {
  document.getElementById("myDropdown").classList.toggle("show");
}

function searchDropdown() {
  document.getElementById("search-box-and-button").classList.toggle("show");
}


// Close the dropdown if the user clicks outside of it
window.onclick = function(event) {
  if (!event.target.matches('.dropbtn')) {
    var dropdowns = document.getElementsByClassName("dropdown-content");
    var i;
    for (i = 0; i < dropdowns.length; i++) {
      var openDropdown = dropdowns[i];
      if (openDropdown.classList.contains('show')) {
        openDropdown.classList.remove('show');
      }
    }
  }
}