param(
    [Parameter(Mandatory=$true)]
    [string]$Message
)

$uri = "http://localhost:3050/hook"
$body = @{
    message = $Message
} | ConvertTo-Json

Invoke-RestMethod -Uri $uri -Method Post -Body $body -ContentType "application/json" | Out-Null
