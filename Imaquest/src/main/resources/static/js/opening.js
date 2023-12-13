// opening.js

// Function to handle skipping the opening and redirecting to the field page
function skipOpening() {
    window.location.href = '/field'; // Change '/field' to the actual path of your field page
}

// Function to handle displaying the story when the title is clicked
document.getElementById('title').addEventListener('click', function () {
    document.getElementById('story').innerHTML = "＊＊＊ Opening Story ＊＊＊"; // Replace with your actual story content
});
