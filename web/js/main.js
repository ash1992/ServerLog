function selectAll(name)
{
    var checkbox = document.getElementById(name);
    var elements = document.getElementsByName(name);
    
    for(var i = 0; i<elements.length; i++)
    {
        elements[i].checked = checkbox.checked;
    }
}

function reSelectAll(name)
{
    var checkbox = document.getElementById(name);
    var elements = document.getElementsByName(name);
    
    var checkAllStatus = true;
    
    for(var i = 0; i<elements.length; i++)
    {
        if(!elements[i].checked)
        {
            checkAllStatus = false;
            break;
        }
    }
    
    checkbox.checked = checkAllStatus;
}

